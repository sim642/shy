package ee.shy.storage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * File locator for having all hashes in the root directory.
 */
public class FlatFileLocator extends FileLocator {
    /**
     * Constructs a new flat file locator with given root.
     * @param root root directory to use
     */
    public FlatFileLocator(Path root) throws IOException {
        super(root);
    }

    @Override
    public Path locate(Hash hash) {
        return root.resolve(hash.toString());
    }
}
