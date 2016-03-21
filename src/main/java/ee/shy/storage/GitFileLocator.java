package ee.shy.storage;

import java.io.File;

/**
 * File locator for having hashes in git-like directory structure in the root directory:
 * first {@link #DIRECTORY_LENGTH} characters of hash represent a subdirectory,
 * remaining hash characters the filename.
 */
public class GitFileLocator extends FileLocator {
    /**
     * Number of hash characters to use for subdirectory name.
     */
    protected static final int DIRECTORY_LENGTH = 2;

    /**
     * Constructs a new git file locator with given root.
     * @param root root directory to use
     */
    public GitFileLocator(File root) {
        super(root);
    }

    @Override
    public File locate(Hash hash) {
        String directory = hash.toString().substring(0, DIRECTORY_LENGTH);
        String filename = hash.toString().substring(DIRECTORY_LENGTH);
        return new File(new File(root, directory), filename);
    }

    @Override
    public File locateAdd(Hash hash) {
        File file = locate(hash);
        return Util.ensurePath(file) ? file : null;
    }
}
