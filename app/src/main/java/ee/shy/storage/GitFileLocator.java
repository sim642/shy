package ee.shy.storage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * File locator for having hashes in git-like directory structure in the root directory:
 * first two characters of hash represent a subdirectory,
 * remaining hash characters the filename.
 */
public class GitFileLocator extends NestedFileLocator {
    /**
     * Constructs a new git file locator with given root.
     * @param root root directory to use
     */
    public GitFileLocator(Path root) throws IOException {
        super(root, 2, 1);
    }
}
