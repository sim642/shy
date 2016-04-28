package ee.shy.storage.accessor;

import ee.shy.storage.FileAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * File accessor to aggregate other file accessors.
 * Allows simultaneous support for multiple accessors.
 */
public class AggregateFileAccessor implements FileAccessor {
    /**
     * List of accessors to use.
     */
    private final List<FileAccessor> accessors;

    /**
     * Constructs a new aggregate file accessor from given accessors
     * @param accessors list of accessors to use
     */
    public AggregateFileAccessor(List<FileAccessor> accessors) {
        this.accessors = accessors;
    }

    /**
     * Adds file content via first file accessor.
     * @param path base path of file to add
     * @param source input stream to get data from
     * @throws IOException if there was a problem reading the input stream or writing to the file
     */
    @Override
    public void add(Path path, InputStream source) throws IOException {
        accessors.get(0).add(path, source); // always use first accessor for add
    }

    /**
     * Gets file content via first successful file accessor.
     * @param path base path of file to get
     * @return input stream to get data from
     * @throws IOException if there was a problem reading from the file
     */
    @Override
    public InputStream get(Path path) throws IOException {
        InputStream source;
        for (FileAccessor accessor : accessors) {
            if ((source = accessor.get(path)) != null)
                return source;
        }
        return null;
    }
}
