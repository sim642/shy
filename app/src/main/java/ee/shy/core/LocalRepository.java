package ee.shy.core;

import ee.shy.io.PathUtils;
import ee.shy.storage.Hash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Repository stored on local filesystem.
 */
public class LocalRepository extends Repository {
    /**
     * Constructs a new local repository class.
     * @param rootPath root directory for repository
     */
    protected LocalRepository(Path rootPath) throws IOException {
        super(rootPath);
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
                return new LocalRepository(currentPath);
            }
            currentPath = currentPath.getParent();
        }
        throw new RepositoryNotFoundException();
    }

    /**
     * Creates a new repository into given directory.
     * @param rootPath directory to initialize in
     * @return a Repository object if repository creation was successful
     * @throws IOException if repository hierarchy generation fails
     */
    public static Repository newEmpty(Path rootPath) throws IOException {
        Path repositoryPath = getRepositoryPath(rootPath);
        Files.createDirectories(repositoryPath);

        String[] subDirectories = {"commit", "branches", "tags", "storage"};
        for (String subDirectory : subDirectories) {
            Files.createDirectory(repositoryPath.resolve(subDirectory));
        }

        CurrentState.newBranch(Hash.ZERO, DEFAULT_BRANCH).write(repositoryPath.resolve("current"));

        Repository repository = new LocalRepository(rootPath);

        // TODO: 26.03.16 Create a config file to home directory upon installation to get author's details from.
        Author author = new Author(null, null);
        repository.setAuthor(author);

        repository.getBranches().put(DEFAULT_BRANCH, new Branch(Hash.ZERO));

        return repository;
    }

    /**
     * Creates a new repository into current directory.
     * @return a Repository object if repository creation was successful
     * @throws IOException if repository hierarchy generation fails
     */
    public static Repository newEmpty() throws IOException {
        return newEmpty(PathUtils.getCurrentPath());
    }
}
