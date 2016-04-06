package ee.shy.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * An interface for accessing file content by base path from a {@link FileLocator}.
 * Used to add/get data via a stream.
 */
public interface FileAccessor {
    /**
     * Adds file content to given base path of file
     * @param path base path of file to add
     * @param source input stream to get data from
     * @throws IOException if there was a problem reading the input stream or writing to the file
     */
    void add(Path path, InputStream source) throws IOException;

    /**
     * Gets file content from given base path of file
     * @param path base path of file to get
     * @return input stream to get data from
     * @throws IOException if there was a problem reading from some input
     */
    InputStream get(Path path) throws IOException;
}
