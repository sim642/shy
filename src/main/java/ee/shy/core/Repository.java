package ee.shy.core;

import java.io.File;
import java.io.IOException;

/**
 * Class for creating and interacting with a repository.
 */
public class Repository {
    /**
     * Dir where shy command was executed.
     */
    private final File rootDirectory;

    /**
     * Repository's root directory.
     */
    private final File repositoryDirectory;

    /**
     * Constructs an empty repository class.
     */
    public Repository() {
        String root = System.getProperty("user.dir");
        this.rootDirectory = new File(root);
        this.repositoryDirectory = new File(root, ".shy");
    }

    /**
     * Create a {@link #repositoryDirectory} directory inside {@link #rootDirectory} if doesn't exist yet.
     * Create directories and files to {@link #repositoryDirectory} described in repository.md
     */
    public void initialize() throws IOException {
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

            System.out.println("Initialized a shy repository in " + rootDirectory.getAbsolutePath());

            // TODO: 23.03.16 Figure out how to write/parse JSON. Add necessary details to author and current.
            //System.out.println(System.getProperty("user.name"));
        } else {
            System.err.println("Directory creation failed!");
        }
    }
}