package ee.shy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An abstract class for locating files by hash.
 * Used to get file paths by hash to use with {@link FileAccessor}.
 */
public abstract class FileLocator {
    /**
     * Root directory to use.
     */
    protected final Path root;

    /**
     * Constructs a new locator with given root.
     * @param root root directory to use
     */
    public FileLocator(Path root) throws IOException {
        //if (!Files.isDirectory(root)) // sshfs workaround
            Files.createDirectories(root);
        this.root = root;
    }

    /**
     * Locates a file by hash.
     * Suitable for overriding both {@link #locateAdd(Hash)} and {@link #locateGet(Hash)}.
     * @param hash hash to get path for
     * @return path to file for given hash
     */
    public Path locate(Hash hash) throws IOException {
        return null;
    }

    /**
     * Locates a supposed file by hash for addition.
     * Suitable for overriding to add extra behavior on addition.
     * @param hash hash to get path for
     * @return supposed path to file for given hash
     */
    public Path locateAdd(Hash hash) throws IOException {
        return locate(hash);
    }

    /**
     * Locates a file by hash for retrieval.
     * Suitable for overriding to add extra behavior on retrieval.
     * @param hash hash to get path for
     * @return path to file for given hash
     */
    public Path locateGet(Hash hash) throws IOException {
        return locate(hash);
    }
}
