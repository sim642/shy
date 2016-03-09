package shy.storage;

import java.io.File;

/**
 * File locator for having all hashes in the root directory.
 */
public class FlatFileLocator extends FileLocator {
    /**
     * Constructs a new flat file locator with given root.
     * @param root root directory to use
     */
    public FlatFileLocator(File root) {
        super(root);
    }

    @Override
    public File locate(Hash hash) {
        return new File(root, hash.toString());
    }
}
