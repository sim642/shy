package ee.shy.io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

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
     * Returns user home directory as path.
     * @return user home directory
     */
    public static Path getUserHomePath() {
        return Paths.get(System.getProperty("user.home"));
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

    /**
     * Recursively delete the given path
     * @param path path to delete
     * @throws IOException if the path can not be visited
     */
    public static void deleteRecursive(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException
            {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
                else {
                    // directory iteration failed
                    throw e;
                }
            }
        });
    }
}
