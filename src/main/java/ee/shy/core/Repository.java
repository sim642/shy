package ee.shy.core;

import ee.shy.io.Json;
import ee.shy.io.PathUtils;
import ee.shy.map.DirectoryJsonMap;
import ee.shy.map.NamedObjectMap;
import ee.shy.storage.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

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

        String[] repositoryFiles = {"author", "current"};
        for (String repositoryFile : repositoryFiles) {
            Files.createFile(repositoryPath.resolve(repositoryFile));
        }

        // TODO: 6.04.16 move current commit handling into separate class
        IOUtils.write(Hash.ZERO.toString(), Files.newOutputStream(repositoryPath.resolve("current")));

        Repository repository = new Repository(repositoryPath.getParent());

        // TODO: 26.03.16 Create a config file to home directory upon installation to get author's details from.
        Author author = new Author(null, null);
        repository.setAuthor(author);

        repository.getBranches().put("master", new Branch(Hash.ZERO));

        System.out.println("Initialized a shy repository in " + repository.getRootPath());
        return repository;
    }

    /**
     * Returns current directory file's path in ".shy/commit/" directory.
     * @param path file which's path to transform
     * @return transformed path in ".shy/commit/"
     */
    private Path getCommitPath(Path path) throws IOException {
        Path commitPath = getRepositoryPath().resolve("commit");
        /*
            Beware of the pitfalls of oh-so-wonderful Path:
            Path#toAbsolutePath does NOT normalize the path to an actual absolute path,
            but simply prepends the current working directory.
         */
        return commitPath.resolve(rootPath.relativize(path.toRealPath()));
    }

    /**
     * Copies given file to its respective directory in ".shy/commit/" directory.
     * @param path file that user wants to add to repository
     * @throws IOException if file can't be found or copying fails
     */
    public void add(Path path) throws IOException {
        Path commitPath = getCommitPath(path);
        /*
            Beware of the pitfalls of oh-so-wonderful Path:
            Files.createDirectories does unintuitive things for paths ending in "..".
            For example, "/tmp/foo/bar/.." will cause "/tmp/foo/bar/" to be created yet it's not in the normalized path.
         */
        Files.createDirectories(commitPath.getParent());
        Files.copy(path, commitPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Removes given file from its directory in ".shy/commit".
     * @param path file that user wants to remove from repository
     * @throws IOException if file could not be deleted
     */
    public void remove(Path path) throws IOException {
        Files.deleteIfExists(getCommitPath(path));
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
        Hash tree = createCommitTree();
        Path currentPath = getRepositoryPath().resolve("current");
        Hash parent = new Hash(IOUtils.toString(Files.newInputStream(currentPath), "UTF-8"));

        Commit commit = new Commit.Builder()
                .setTree(tree)
                .addParent(parent)
                .setAuthor(getAuthor())
                .setTimeCurrent()
                .setMessage(message)
                .create();
        Hash hash = storage.put(commit);

        branches.put("master", new Branch(hash)); // TODO: 26.03.16 update correct branch
        IOUtils.write(hash.toString(), Files.newOutputStream(currentPath));
    }

    /**
     * Get the Author object of this repository.
     * @return Author object of '.shy/author' file
     * @throws IOException if file '.shy/author' does not exist or reading fails
     */
    public Author getAuthor() throws IOException {
        return Json.read(Files.newInputStream(getAuthorPath()), Author.class);
    }

    /**
     * Set the '.shy/author' file contents to given Author file contents.
     * @param author Author object that is assigned to this repository
     * @throws IOException if write fails
     */
    public void setAuthor(Author author) throws IOException {
        author.write(Files.newOutputStream(getAuthorPath()));
    }

    public NamedObjectMap<Branch> getBranches() {
        return branches;
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