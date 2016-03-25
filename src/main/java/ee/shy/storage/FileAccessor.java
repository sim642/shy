package ee.shy.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * An interface for accessing file content by base path from a {@link FileLocator}.
 * Used to add/get data via a stream.
 */
public interface FileAccessor {
    /**
     * Adds file content to given base path of file
     * @param file base path of file to add
     * @param source input stream to get data from
     * @throws IOException if there was a problem reading the input stream or writing to the file
     */
    void add(File file, InputStream source) throws IOException;

    /**
     * Gets file content from given base path of file
     * @param file base path of file to get
     * @return input stream to get data from
     * @throws IOException if there was a problem reading from some input
     */
    InputStream get(File file) throws IOException;
}
