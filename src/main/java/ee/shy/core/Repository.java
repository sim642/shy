package ee.shy.core;

import difflib.PatchFailedException;
import ee.shy.core.diff.TreeDiffer;
import ee.shy.core.merge.TreeMerger;
import ee.shy.io.Json;
import ee.shy.io.PathUtils;
import ee.shy.map.DirectoryJsonMap;
import ee.shy.map.NamedObjectMap;
import ee.shy.storage.DataStorage;
import ee.shy.storage.FileStorage;
import ee.shy.storage.Hash;
import ee.shy.storage.Hashed;
import ee.shy.storage.accessor.PlainFileAccessor;
import ee.shy.storage.locator.FlatFileLocator;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for creating and interacting with a repository.
 */
public class Repository {
    /**
     * Repository root directory path.
     */
    private final Path rootPath;

    private final DataStorage storage;
    private final NamedObjectMap<Branch> branches;
    private final NamedObjectMap<Tag> tags;

    /**
     * Repository's current checked out state.
     */
    private CurrentState current;

    /**
     * Constructs a new repository class.
     * @param rootPath root directory for repository
     */
    private Repository(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        this.storage = new FileStorage(
                Arrays.asList(
                        new FlatFileLocator(getRepositoryPath().resolve("storage"))
                ),
                new PlainFileAccessor());
        branches = new DirectoryJsonMap<>(Branch.class, getRepositoryPath().resolve("branches"));
        tags = new DirectoryJsonMap<>(Tag.class, getRepositoryPath().resolve("tags"));
        current = Json.read(getRepositoryPath().resolve("current"), CurrentState.class);
    }

    /**
     * Tries to find an existing repository in the directory shy was executed or its parent directories.
     * @return repository object if existing repository was found, null otherwise
     */
    public static Repository newExisting() throws RepositoryNotFoundException, IOException {
        Path currentPath = PathUtils.getCurrentPath();
        while (currentPath != null) {
            Path repositoryPath = getRepositoryPath(currentPath);
            if (Files.isDirectory(repositoryPath)) {
                return new Repository(currentPath);
            }
            currentPath = currentPath.getParent();
        }
        throw new RepositoryNotFoundException();
    }

    /**
     * Creates a new repository in the directory where shy was executed.
     * @return a Repository object if repository creation was successful
     * @throws IOException if repository hierarchy generation fails
     */
    public static Repository newEmpty() throws IOException {
        Path repositoryPath = getRepositoryPath(PathUtils.getCurrentPath());
        Files.createDirectory(repositoryPath);

        String[] subDirectories = {"commit", "branches", "tags", "storage"};
        for (String subDirectory : subDirectories) {
            Files.createDirectory(repositoryPath.resolve(subDirectory));
        }

        CurrentState.newBranch(Hash.ZERO, "master").write(repositoryPath.resolve("current"));

        Repository repository = new Repository(repositoryPath.getParent());

        // TODO: 26.03.16 Create a config file to home directory upon installation to get author's details from.
        Author author = new Author(null, null);
        repository.setAuthor(author);

        repository.getBranches().put("master", new Branch(Hash.ZERO));

        System.out.println("Initialized a shy repository in " + repository.getRootPath());
        return repository;
    }

    /**
     * Returns repository's ".shy/commit/" directory path.
     * @return commit directory path
     */
    private Path getCommitPath() {
        return getRepositoryPath().resolve("commit");
    }

    /**
     * Returns current directory file's path in ".shy/commit/" directory.
     * @param path file which's path to transform
     * @return transformed path in ".shy/commit/"
     */
    private Path getCommitPath(Path path) throws IOException {
        /*
            Beware of the pitfalls of oh-so-wonderful Path:
            Path#toAbsolutePath does NOT normalize the path to an actual absolute path,
            but simply prepends the current working directory.
         */
        return getCommitPath().resolve(rootPath.relativize(path.toAbsolutePath().normalize()));
    }

    /**
     * Returns commit directory files' path in root directory.
     * @param path file which's path to transform
     * @return transformed path in root directory
     */
    private Path getCurrentPath(Path path) throws IOException {
        return getRootPath().resolve(getCommitPath().relativize(path.toRealPath()));
    }

    /**
     * Copies given file to its respective directory in ".shy/commit/" directory.
     * Forces creation (see {@link #add(Path, boolean)}.
     * @param path file that user wants to add to repository
     * @throws IOException if file can't be found, copying fails or path is of unknown type
     */
    public void add(Path path) throws IOException {
        add(path, true);
    }

    /**
     * Copies given file to its respective directory in ".shy/commit/" directory.
     * @param path file that user wants to add to repository
     * @param force whether addition should be forced, e.g. creation of hidden item
     * @throws IOException if file can't be found, copying fails or path is of unknown type
     */
    public void add(Path path, boolean force) throws IOException {
        if (force || !Files.isHidden(path)) {
            if (Files.isRegularFile(path)) {
                Path commitPath = getCommitPath(path);
                /*
                    Beware of the pitfalls of oh-so-wonderful Path:
                    Files.createDirectories does unintuitive things for paths ending in "..".
                    For example, "/tmp/foo/bar/.." will cause "/tmp/foo/bar/" to be created yet it's not in the normalized path.
                 */
                PathUtils.createParentDirectories(commitPath);
                Files.copy(path, commitPath, StandardCopyOption.REPLACE_EXISTING);
            }
            else if (Files.isDirectory(path)) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
                    for (Path innerPath : directoryStream) {
                        add(innerPath, false);
                    }
                }
            }
            else {
                throw new IOException("addable path (" + path + ") is neither file nor directory");
            }
        }
    }

    /**
     * Removes given file from its directory in ".shy/commit".
     * @param path file that user wants to remove from repository
     * @throws IOException if file could not be deleted or path is of unknown type
     */
    public void remove(Path path) throws IOException {
        Path commitPath = getCommitPath(path);
        if (Files.isRegularFile(commitPath)) {
            Files.deleteIfExists(commitPath);
        }
        else if (Files.isDirectory(commitPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(commitPath)) {
                for (Path innerPath : directoryStream) {
                    remove(getCurrentPath(innerPath));
                }
            }

            if (!Files.isSameFile(getCommitPath(), commitPath)) // prevent "./shy/commit/" from being deleted
                Files.delete(commitPath);
        }
        else {
            throw new IOException("removable path (" + path + ") is neither file nor directory");
        }
    }

    /**
     * Creates and stores the complete ".shy/commit/" directory tree.
     * @return hash of stored tree
     * @throws IOException if there was a problem storing the tree
     */
    private Hash createCommitTree() throws IOException {
        Tree tree = new Tree.Builder(storage).fromDirectory(getRepositoryPath().resolve("commit")).create();
        return storage.put(tree);
    }

    /**
     * Commits current commit with given message.
     * @param message commit message
     * @throws IOException if there was a problem storing the tree/commit or modifying ".shy/current"
     */
    public void commit(String message, Hash revisedParent, Tree mergedTree) throws IOException {
        if (current.getType() != CurrentState.Type.BRANCH) {
            throw new CommitException(current);
        }

        Hash tree;
        if (mergedTree != null) {
            tree = storage.put(mergedTree);
        } else {
            tree = createCommitTree();
        }

        Commit.Builder commitBuilder = new Commit.Builder()
                .setTree(tree)
                .addParent(current.getCommit())
                .setAuthor(getAuthor())
                .setTimeCurrent()
                .setMessage(message);
        if (revisedParent != null)
            commitBuilder.addParent(revisedParent);

        Commit commit = commitBuilder.create();
        Hash hash = storage.put(commit);

        CurrentState newCurrent = CurrentState.newBranch(hash, current.getBranch());
        setCurrent(newCurrent);

        branches.put(newCurrent.getBranch(), new Branch(newCurrent.getCommit()));
    }

    /**
     * Parses matching {@link CurrentState} object from given string argument.
     * Used for uniform commit and branch argument handling.
     * @param str string argument to parse
     * @return current state object for argument
     * @throws IOException
     */
    public CurrentState parseState(String str) throws IOException {
        if (str == null || str.isEmpty())
            return current;
        if (branches.containsKey(str))
            return CurrentState.newBranch(branches.get(str).getHash(), str);
        else if (tags.containsKey(str))
            return CurrentState.newTag(tags.get(str).getHash(), str);
        else {
            Hash hash = new Hash(str);
            return CurrentState.newCommit(hash);
        }
    }

    /**
     * Checkouts a branch or commit
     * @param arg a branch or a commit to checkout to
     * @throws IOException
     */
    public void checkout(String arg) throws IOException {
        CurrentState newCurrent = parseState(arg);

        Commit commit = storage.get(newCurrent.getCommit(), Commit.class);
        if (commit != null) {
            Tree tree = storage.get(commit.getTree(), Tree.class);

            PathUtils.deleteRecursive(getCommitPath());
            Files.createDirectory(getCommitPath());

            tree.toDirectory(getCommitPath(), storage);
            tree.toDirectory(getRootPath(), storage);

            setCurrent(newCurrent);
        }
    }

    /**
     * A method that calls out log builder of given commit
     * @param arg string of branch name or commit hash
     * @throws IOException if building fails
     */
    public List<Hashed<Commit>> log(String arg) throws IOException {
        return log(parseState(arg).getCommit());
    }

    /**
     * Builds a history log of a commit and it's parents' commits
     * @param commitHash hash of a commit that's history log is wanted to be built
     * @throws IOException if getting the commit fails
     */
    private List<Hashed<Commit>> log(Hash commitHash) throws IOException {
        List<Hashed<Commit>> loggedCommits = new ArrayList<>();
        if (!commitHash.equals(Hash.ZERO)) {
            Commit commit = storage.get(commitHash, Commit.class);
            loggedCommits.add(new Hashed(commitHash, commit));
            List<Hash> parents = commit.getParents();
            for (Hash parent : parents) {
                loggedCommits.addAll(log(parent));
            }
        }
        return loggedCommits;
    }

    /**
     * Searches for given expression in a commit specified by argument.
     * @param arg string referencing a commit
     * @param expression regular expression string to search for
     */
    public void search(String arg, String expression) throws IOException {
        commitSearch(parseState(arg).getCommit(), expression);
    }

    /**
     * Searches for given expression from given commit's tree
     * @param commitHash hash of the commit
     * @param expression string of the expression
     * @throws IOException if there was a problem walking the tree or getting input stream from storage
     */
    private void commitSearch(Hash commitHash, String expression) throws IOException {
        Commit commit = storage.get(commitHash, Commit.class);
        Tree tree = storage.get(commit.getTree(), Tree.class);
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher("");

        List<String> instances = new ArrayList<>();
        tree.walk(storage, new TreeVisitor() {
            @Override
            public void visitFile(TreePath path, InputStream is) throws IOException {
                instances.addAll(findInstance(path, is, matcher));
            }
        });

        for (String instance : instances) {
            System.out.println(instance);
        }
    }

    /**
     * Searches for an expression from an input stream
     * @param path path to file
     * @param is input stream to read from
     * @param matcher matcher to use for matching
     * @return instances found in a file
     * @throws IOException if establishing streams fails
     */
    private static List<String> findInstance(TreePath path, InputStream is, Matcher matcher) throws IOException {
        List<String> foundInstances = new ArrayList<>();
        try (Reader reader = new InputStreamReader(is);
             LineNumberReader lineReader = new LineNumberReader(reader)) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                matcher.reset(line);
                if (matcher.find()) {
                    foundInstances.add(String.format("%s:%d[%d,%d]:%s",
                            path, lineReader.getLineNumber(), matcher.start(), matcher.end(), line));
                }
            }
        }
        return foundInstances;
    }

    /**
     * Get the Author object of this repository.
     * @return Author object of '.shy/author' file
     * @throws IOException if file '.shy/author' does not exist or reading fails
     */
    public Author getAuthor() throws IOException {
        return Json.read(getAuthorPath(), Author.class);
    }

    /**
     * Set the '.shy/author' file contents to given Author file contents.
     * @param author Author object that is assigned to this repository
     * @throws IOException if write fails
     */
    public void setAuthor(Author author) throws IOException {
        author.write(getAuthorPath());
    }

    public NamedObjectMap<Branch> getBranches() {
        return branches;
    }

    public NamedObjectMap<Tag> getTags() {
        return tags;
    }

    /**
     * Returns the current checked out state.
     * @return current checked out state
     */
    public CurrentState getCurrent() {
        return current;
    }

    /**
     * Sets the current checked out state.
     * @param current state to set to
     * @throws IOException if writing state to file failed
     */
    private void setCurrent(CurrentState current) throws IOException {
        this.current = current;
        current.write(getRepositoryPath().resolve("current"));
    }

    /**
     * Returns repository's root path.
     * @return root directory path
     */
    public Path getRootPath() {
        return rootPath;
    }

    /**
     * Returns repository's ".shy/" directory path in given root directory.
     * @param rootPath root directory of repository
     * @return repository directory path
     */
    private static Path getRepositoryPath(Path rootPath) {
        return rootPath.resolve(".shy");
    }

    /**
     * Returns repository's ".shy/" directory path.
     * @return repository directory path
     */
    private Path getRepositoryPath() {
        return getRepositoryPath(rootPath);
    }

    /**
     * Returns repository's "author" file path.
     * @return author file path
     */
    private Path getAuthorPath() {
        return getRepositoryPath().resolve("author");
    }

    /**
     * Differences two commits specified by arguments.
     * @param arg1 string referencing original commit
     * @param arg2 string referencing revised commit
     * @return lines of diff output
     * @throws IOException
     */
    public List<String> diff(String arg1, String arg2) throws IOException {
        return getCommitDiff(parseState(arg1).getCommit(), parseState(arg2).getCommit());
    }

    /**
     * Takes two commit hashes as parameters and returns a list of Strings containing
     * the differences between given commits.
     * @param original Hash of the original commit
     * @param revised Hash of the revised commit
     * @return Diff strings
     * @throws IOException
     */
    private List<String> getCommitDiff(Hash original, Hash revised) throws IOException {
        TreeDiffer treeDiffer = new TreeDiffer(storage);
        Commit originalCommit = storage.get(original, Commit.class);
        Commit revisedCommit = storage.get(revised, Commit.class);
        return treeDiffer.diff(
                storage.get(originalCommit.getTree(), Tree.class),
                storage.get(revisedCommit.getTree(), Tree.class));
    }

    /**
     * Takes two commit hashes as parameters and returns the hash of the commit
     * which is the last common ancestor for both given commits.
     * If no common ancestor, return null instead.
     * @param first Commit hash
     * @param second Commit hash
     * @return hash of common ancestor if exists, null otherwise
     * @throws IOException
     */
    private Hash findCommonAncestor(Hash first, Hash second) throws IOException {
        // TODO: 15.05.16 Algorithm incomplete, Simmo pls fix
        Set<Hash> visitedHashes = new HashSet<>();
        while (!first.equals(Hash.ZERO) || !second.equals(Hash.ZERO)) {
            if (!first.equals(Hash.ZERO)) {
                if (!visitedHashes.add(first)) {
                    return first;
                }
                first = storage.get(first, Commit.class).getParents().get(0);
            }
            if(!second.equals(Hash.ZERO)) {
                if (!visitedHashes.add(second)) {
                    return second;
                }
                second = storage.get(second, Commit.class).getParents().get(0);
            }
        }
        return null;
    }

    public void merge(String originalArg, String revisedArg) throws IOException, PatchFailedException {
        TreeMerger treeMerger = new TreeMerger(storage);
        Hash originalCommitHash = parseState(originalArg).getCommit();
        Hash revisedCommitHash = parseState(revisedArg).getCommit();
        Commit originalCommit = storage.get(originalCommitHash, Commit.class);
        Commit revisedCommit = storage.get(revisedCommitHash, Commit.class);

        Tree mergedTree = treeMerger.merge(
                storage.get(originalCommit.getTree(), Tree.class),
                storage.get(revisedCommit.getTree(), Tree.class),
                storage.get(findCommonAncestor(revisedCommitHash, originalCommitHash), Tree.class)
        );

        commit("Merge commit", revisedCommitHash, mergedTree);
        // TODO: 15.05.16 Merge commit message should be something meaningful
    }
}