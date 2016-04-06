package ee.shy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utilities class for storage-related functions.
 */
public class Util {
    private Util() {

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
     * Ensures file path's validity by creating required missing parent directories.
     * @param path file path which's validity to ensure
     */
    public static void ensurePath(Path path) throws IOException {
        if (!Files.isDirectory(path))
            Files.createDirectories(path.getParent());
    }
}
