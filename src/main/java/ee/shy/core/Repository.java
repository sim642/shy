package ee.shy.core;

import ee.shy.storage.Hash;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;
import java.nio.file.Files;


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

    /**
     * Constructs a new repository class.
     */
    private Repository(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.repositoryDirectory = new File(rootDirectory, ".shy");
    }

    /**
     * Tries to find an existing repository in the directory shy was executed or its parent directories.
     * @return repository object if existing repository was found, null otherwise.
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
     * @return a Repository object if repository creation was successful. IOException will be thrown otherwise.
     * @throws IOException
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
        } else {
            throw new IOException("Repository initialization failed!");
        }
    }

    public void add(File file) throws IOException {
        File currentDirectory = new File(System.getProperty("user.dir"));
        //System.out.println(currentDirectory.toString());
        File repositoryDirectory = new File(currentDirectory.getPath(), ".shy/commit/");
        //System.out.println(repositoryDirectory.toString());
        String[] sFilePath = file.toString().split("/");
        File fileDir = new File(sFilePath[0]);
        String sFileName = sFilePath[sFilePath.length - 1];

        if(file.toString().lastIndexOf("/") > 0) {
            for(int i = 1; i < sFilePath.length - 1; i++){
                fileDir = new File(fileDir.toString(), sFilePath[i]);
            }
            //System.out.println(fileDir.toString());
        }
        if(! new File(repositoryDirectory.getPath(), fileDir.getPath()).exists()) {
            new File(repositoryDirectory.getPath(), fileDir.getPath()).getParentFile().mkdirs();
        }

        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(file);
            output = new FileOutputStream(new File(repositoryDirectory.getPath(), file.getPath()));
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}