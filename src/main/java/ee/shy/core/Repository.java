package ee.shy.core;

import java.io.File;
import java.io.IOException;

/**
 * 
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
     * @param rootDirectory root directory to use
     * @param repositoryDirectory repository's root directory
     */
    public Repository(File rootDirectory, File repositoryDirectory) {
        this.rootDirectory = rootDirectory;
        this.repositoryDirectory = repositoryDirectory;
    }

    /**
     * Create a {@link #repositoryDirectory} directory inside {@link #rootDirectory} if doesn't exist yet.
     * Create directories and files to {@link #repositoryDirectory} described in repository.md
     */
    public void initialize() {
        if (repositoryDirectory.exists()) return;
        if (repositoryDirectory.mkdir()) {
            String[] subDirectories = {"commit", "branches", "tags", "storage"};
            for (String subDirectory : subDirectories) {
                new File(repositoryDirectory.getAbsolutePath() + "/" + subDirectory).mkdir();
            }
            File author = new File(repositoryDirectory + "/author");
            File current = new File(repositoryDirectory + "/current");
            try {
                author.createNewFile();
                current.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // TODO: 23.03.16 Figure out how to write/parse JSON. Add necessary details to author and current.
            //System.out.println(System.getProperty("user.name"));
        } else {
            System.err.println("Directory creation failed!");
        }
    }
}