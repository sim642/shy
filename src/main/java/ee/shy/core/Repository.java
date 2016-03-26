package ee.shy.core;

import ee.shy.storage.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * Class for creating and interacting with a repository.
 */
public class Repository {
    /**
     * Repository's root directory.
     */
    private final File repositoryDirectory;

    /**
     * {@link #repositoryDirectory}'s parent directory.
     */
    private final File rootDirectory;

    private final DataStorage storage;

    /**
     * Constructs a new repository class.
     * @param rootDirectory root directory for repository
     */
    private Repository(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.repositoryDirectory = new File(rootDirectory, ".shy");
        this.storage = new FileStorage(
                Arrays.asList(
                        new FlatFileLocator(new File(repositoryDirectory, "storage"))
                ),
                new PlainFileAccessor());
    }

    /**
     * Tries to find an existing repository in the directory shy was executed or its parent directories.
     * @return repository object if existing repository was found, null otherwise
     */
    public static Repository newExisting() throws RepositoryNotFoundException {
        File currentDirectory = new File(System.getProperty("user.dir"));
        while (currentDirectory != null) {
            File repositoryDirectory = new File(currentDirectory, ".shy");
            if (repositoryDirectory.exists() && repositoryDirectory.isDirectory()) {
                return new Repository(currentDirectory);
            }
            currentDirectory = currentDirectory.getParentFile();
        }
        throw new RepositoryNotFoundException();
    }

    /**
     * Creates a new repository in the directory where shy was executed.
     * @return a Repository object if repository creation was successful
     * @throws IOException if repository hierarchy generation fails
     */
    public static Repository newEmpty() throws IOException {
        File currentDirectory = new File(System.getProperty("user.dir"));
        File repositoryDirectory = new File(currentDirectory, ".shy");
        if (repositoryDirectory.exists() || repositoryDirectory.mkdir()) {
            String[] subDirectories = {"commit", "branches", "tags", "storage"};
            for (String subDirectory : subDirectories) {
                File f = new File(repositoryDirectory, subDirectory);
                if (!f.exists())
                    f.mkdir();
            }

            String[] repositoryFiles = {"author", "current"};
            for (String repositoryFile : repositoryFiles) {
                File f = new File(repositoryDirectory, repositoryFile);
                if (!f.exists())
                    f.createNewFile();
            }

            // The following will be refactored later in the project development(Phase 2)
            File master = new File(new File(repositoryDirectory, "branches"), "master");
            File current = new File(repositoryDirectory, "current");
            master.createNewFile();

            TeeOutputStream teeOutputStream = new TeeOutputStream(new FileOutputStream(master), new FileOutputStream(current));
            teeOutputStream.write(Hash.ZERO.toString().getBytes());
            teeOutputStream.close();

            System.out.println("Initialized a shy repository in " + currentDirectory.getAbsolutePath());

            return new Repository(currentDirectory);

            // TODO: 23.03.16 Figure out how to write/parse JSON. Add necessary details to author
            //System.out.println(System.getProperty("user.name"));
        }
        else {
            throw new IOException("Repository initialization failed!");
        }
    }

    /**
     * Copies given file to its respective directory in ".shy/commit/" directory.
     * @param file file that user wants to add to repository
     * @throws IOException if file can't be found or copying fails
     */
    public void add(File file) throws IOException {
        File fullFilePath = fullFilePath(file);
        fullFilePath.getParentFile().mkdirs();
        try (InputStream input = new FileInputStream(file)) {
            try (OutputStream output = new FileOutputStream(fullFilePath)) {
                IOUtils.copy(input, output);
            }
        }
    }

    /**
     * Removes given file from its directory in ".shy/commit".
     * @param file file that user wants to remove from repository
     * @throws IOException if file could not be deleted
     */
    public void remove(File file) throws IOException {
        Files.deleteIfExists(fullFilePath(file).toPath());
    }

    public Hash createCommitTree() throws IOException {
        Tree tree = new Tree.Builder(storage).fromDirectory(new File(repositoryDirectory, "commit")).create();
        return storage.add(tree.inputify());
    }

    /**
     * Creates given file's path relative to repository's directory.
     * @param file file that's relative path is wanted to be create
     * @return file's path relative to repository's directory
     */
    private File relativeFilePath(File file) {
        File fileDir = new File(System.getProperty("user.dir"), file.getPath()).getParentFile();

        Path path = Paths.get(fileDir.getPath());
        path = this.rootDirectory.toPath().relativize(path);

        return new File(path.toFile().getPath(), file.getName());
    }

    /**
     * Creates full path from system's root to given file in ".shy/commit/" directory.
     * @param file file that's path is wanted to be create
     * @return file path from system's root to given file'is directory in ".shy/commit/"
     */
    private File fullFilePath(File file) {
        File repositoryDirectory = new File(this.rootDirectory.getPath(), ".shy/commit/");
        return new File(repositoryDirectory, relativeFilePath(file).getPath());
    }
}