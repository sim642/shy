package shy.storage;

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
     * @throws IOException
     */
    void add(File file, InputStream source) throws IOException;

    /**
     * Gets file content from given base path of file
     * @param file base path of file to get
     * @return input stream to get data from
     * @throws IOException
     */
    InputStream get(File file) throws IOException;
}
