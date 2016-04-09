import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class TemporaryDirectory extends ExternalResource {
    private final Path parentDirectory;
    private Path directory;

    public TemporaryDirectory() {
        this(null);
    }

    public TemporaryDirectory(Path parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

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
    }

    public Path newFile(String fileName) throws IOException {
        Path path = getRoot().resolve(fileName);
        Files.createFile(path);
        return path;
    }

    public Path newFile() throws IOException {
        return Files.createTempFile(getRoot(), "junit", null);
    }

    public Path newDirectory(String directoryName) throws IOException {
        return newDirectory(Paths.get(directoryName));
    }

    public Path newDirectory(Path path) throws IOException {
        Path fullPath = getRoot().resolve(path);
        Files.createDirectories(fullPath);
        return fullPath;
    }

    public Path newDirectory() throws IOException {
        return Files.createTempDirectory(getRoot(), null);
    }

    protected void create() throws IOException {
        directory = createTemporaryDirectoryIn(parentDirectory);
    }

    private Path createTemporaryDirectoryIn(Path parentDirectory) throws IOException {
        if (parentDirectory != null)
            return Files.createTempDirectory(parentDirectory, "junit");
        else
            return Files.createTempDirectory("junit");
    }

    protected void delete() throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
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
                } else {
                    // directory iteration failed
                    throw e;
                }
            }
        });
    }

    public Path getRoot() {
        if (directory == null)
            throw new IllegalStateException("temporary directory has not been created");
        return directory;
    }
}
