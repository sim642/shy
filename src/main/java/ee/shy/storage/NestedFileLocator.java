package ee.shy.storage;

import java.io.File;

/**
 * File locator for having hashes in nested directory structure in the root directory:
 * every {@link #directoryLength} characters of hash represent a subdirectory,
 * limited by having at most {@link #depthMax} subdirectories,
 * remaining hash characters the filename.
 */
public class NestedFileLocator extends FileLocator {
    /**
     * Number of hash characters to use for each subdirectory name.
     */
    private final int directoryLength;

    /**
     * Maximum nesting depth.
     */
    private final int depthMax;

    /**
     * Constant to represent unlimited maximum depth.
     */
    public static final int DEPTH_UNLIMITED = -1;

    /**
     * Constructs a new nested file locator with given root with unlimited nesting depth.
     * @param root root directory to use
     * @param directoryLength length of each subdirectory name
     */
    public NestedFileLocator(File root, int directoryLength) {
        this(root, directoryLength, DEPTH_UNLIMITED);
    }

    /**
     * Constructs a new nested file locator with given root.
     * @param root root directory to use
     * @param directoryLength length of each subdirectory name
     * @param depthMax maximum nesting depth, unlimited with {@link #DEPTH_UNLIMITED}
     */
    public NestedFileLocator(File root, int directoryLength, int depthMax) {
        super(root);
        this.directoryLength = directoryLength;
        this.depthMax = depthMax;
    }

    @Override
    public File locate(Hash hash) {
        String hashString = hash.toString();
        File file = root;

        int depth;
        for (depth = 0; (depthMax == DEPTH_UNLIMITED || depth < depthMax) && (directoryLength * (depth + 1) < hashString.length()); depth++)
            file = new File(file, hashString.substring(directoryLength * depth, directoryLength * (depth + 1)));

        file = new File(file, hashString.substring(directoryLength * depth));
        return file;
    }

    @Override
    public File locateAdd(Hash hash) {
        File file = locate(hash);
        return Util.ensurePath(file) ? file : null;
    }
}
