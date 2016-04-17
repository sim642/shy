package ee.shy.core;

import ee.shy.io.Json;
import ee.shy.io.PathUtils;
import ee.shy.map.DirectoryJsonMap;
import ee.shy.map.NamedObjectMap;
import ee.shy.storage.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

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
        return getCommitPath().resolve(rootPath.relativize(path.toRealPath()));
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
    public void commit(String message) throws IOException {
        if (current.getType() == CurrentState.Type.BRANCH) {
            Hash tree = createCommitTree();

            Commit commit = new Commit.Builder()
                    .setTree(tree)
                    .addParent(current.getCommit())
                    .setAuthor(getAuthor())
                    .setTimeCurrent()
                    .setMessage(message)
                    .create();
            Hash hash = storage.put(commit);

            CurrentState newCurrent = CurrentState.newBranch(hash, current.getBranch());
            setCurrent(newCurrent);

            branches.put(newCurrent.getBranch(), new Branch(newCurrent.getCommit()));
        }
        else
            throw new RuntimeException("can't commit to " + current); // TODO: 15.04.16 create custom exception
    }

    /**
     *  A method that calls out log builder with current commit if no params are given
     * @throws IOException if getting commit hash from a branch fails or building fails
     */
    public void log() throws IOException {
        buildLog(branches.get(current.getBranch()).getHash());
    }

    /**
     * A method that calls out log builder of given commit
     * @param toBuild string of branch name or commit hash
     * @throws IOException if building fails
     */
    public void log(String toBuild) throws IOException {
        if(branches.containsKey(toBuild)) {
            buildLog(branches.get(toBuild).getHash());
        }
        else {
            buildLog(new Hash(toBuild));
        }
    }

    /**
     * Builds a history log of a commit and it's parents' commits
     * @param commitHash hash of a commit that's history log is wanted to be built
     * @throws IOException if getting the commit fails
     */
    private void buildLog(Hash commitHash) throws IOException {
        if (!commitHash.equals(Hash.ZERO)) {
            Commit commit = storage.get(commitHash, Commit.class);

            System.out.println("Commit: " + commitHash.toString());

            Author author = commit.getAuthor();
            System.out.println("Author: " + author.getName() + "<" + author.getEmail() + ">");

            System.out.println("Time: " + commit.getTime());

            System.out.println("\n \t" + "Message: " + commit.getMessage() + "\n");

            List<Hash> parents = commit.getParents();
            for (Hash parent : parents) {
                log(parent.toString());
            }
        }
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
}