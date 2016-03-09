package shy.storage;

import java.io.File;

/**
 * An abstract class for locating files by hash.
 * Used to get file paths by hash to use with {@link FileAccessor}.
 */
public abstract class FileLocator {
    /**
     * Root directory to use.
     */
    protected final File root;

    /**
     * Constructs a new locator with given root.
     * @param root root directory to use
     */
    public FileLocator(File root) {
        if (!root.exists())
            root.mkdirs();
        if (!root.isDirectory())
            throw new RuntimeException("root must be a directory"); // TODO: 9.03.16 throw better exception

        this.root = root;
    }

    /**
     * Locates a file by hash.
     * Suitable for overriding both {@link #locateAdd(Hash)} and {@link #locateGet(Hash)}.
     * @param hash hash to get path for
     * @return path to file for given hash
     */
    public File locate(Hash hash) {
        return null;
    }

    /**
     * Locates a supposed file by hash for addition.
     * Suitable for overriding to add extra behavior on addition.
     * @param hash hash to get path for
     * @return supposed path to file for given hash
     */
    public File locateAdd(Hash hash) {
        return locate(hash);
    }

    /**
     * Locates a file by hash for retrieval.
     * Suitable for overriding to add extra behavior on retrieval.
     * @param hash hash to get path for
     * @return path to file for given hash
     */
    public File locateGet(Hash hash) {
        return locate(hash);
    }
}
