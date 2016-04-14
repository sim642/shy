package ee.shy.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilities class for I/O-related methods.
 */
public final class PathUtils {
    private PathUtils() {

    }

    /**
     * Returns current working directory as path.
     * @return current working directory
     */
    public static Path getCurrentPath() {
        return Paths.get(System.getProperty("user.dir"));
    }

    /**
     * Adds given extension to file path.
     * @param path file path to extend
     * @param extension extension to add
     * @return file path with given extension
     */
    public static Path addExtension(Path path, String extension) {
        return path.resolveSibling(path.getFileName().toString() + extension);
    }

    /**
     * Creates given path's parent directories.
     * @param path path which's parent directories to create
     */
    public static void createParentDirectories(Path path) throws IOException {
        if (Files.notExists(path))
            Files.createDirectories(path.getParent());
    }
}
