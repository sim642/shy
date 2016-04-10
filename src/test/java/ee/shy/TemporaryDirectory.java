package ee.shy;

import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/*
    JUnit may be great but TemporaryFolder isn't. It uses File but File is satan!
    I want to use Path but JUnit won't let me so I have to suffer under its archaic limitations.
    As an alternative I can .toPath() and .toFile() all over so my eyes continue to bleed.

    Horrendous API design is the root of all evil, there is no solution other than duplicating everything properly,
    but that's a programming sin as well. Crappy APIs doom not only themselves but every other project using them
    eventually dooming the entire world.

    And that, folks, is why TemporaryDirectory is a thing, seeking to save the world of its miseries.
 */

/**
 * {@link org.junit.rules.TemporaryFolder} replacement which works using {@link Path}.
 *
 * @see org.junit.rules.TemporaryFolder
 */
public class TemporaryDirectory extends ExternalResource {
    /**
     * Prefix used for unnamed temporary directories and files.
     */
    private static final String TEMPORARY_PREFIX = "junit";

    /**
     * Root path of given temporary directory.
     */
    private Path root;

    @Override
    protected void before() throws Throwable {
        create();
    }

    @Override
    protected void after() {
        try {
            delete();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            root = null; // restore null value after cleanup (unlike TemporaryFolder)
        }
    }

    /**
     * Creates a new file with given path in the temporary directory.
     * @param path relative path of file
     * @return full path of file
     * @throws IOException
     */
    public Path newFile(Path path) throws IOException {
        Path fullPath = getRoot().resolve(path);
        // TODO: 10.04.16 create parent directories for path
        Files.createFile(path);
        return fullPath;
    }

    /**
     * Creates a new file with given path components in the temporary directory.
     * @param first initial component of path
     * @param more additional components of path
     * @return full path of file
     * @throws IOException
     */
    public Path newFile(String first, String... more) throws IOException {
        return newFile(Paths.get(first, more));
    }

    /**
     * Creates a new file with random name in the temporary directory.
     * @return full path of file
     * @throws IOException
     */
    public Path newFile() throws IOException {
        return Files.createTempFile(getRoot(), TEMPORARY_PREFIX, null);
    }

    /**
     * Creates a new directory with given path in the temporary directory.
     * @param path relative path of directory
     * @return full path of directory
     * @throws IOException
     */
    public Path newDirectory(Path path) throws IOException {
        Path fullPath = getRoot().resolve(path);
        Files.createDirectories(fullPath);
        return fullPath;
    }

    /**
     * Creates a new directory with given path components in the temporary directory.
     * @param first initial component of path
     * @param more additional components of path
     * @return full path of directory
     * @throws IOException
     */
    public Path newDirectory(String first, String... more) throws IOException {
        return newDirectory(Paths.get(first, more));
    }

    /**
     * Creates a new directory with random name in the temporary directory.
     * @return full path of directory
     * @throws IOException
     */
    public Path newDirectory() throws IOException {
        return Files.createTempDirectory(getRoot(), TEMPORARY_PREFIX);
    }

    /**
     * Returns the root path of the temporary directory.
     * @return root path
     */
    public Path getRoot() {
        if (root == null)
            throw new IllegalStateException("temporary directory has not been created");
        return root;
    }

    protected void create() throws IOException {
        root = Files.createTempDirectory(TEMPORARY_PREFIX);
    }

    protected void delete() throws IOException {
        Files.walkFileTree(getRoot(), new SimpleFileVisitor<Path>() {
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
