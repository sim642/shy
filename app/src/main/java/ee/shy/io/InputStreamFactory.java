package ee.shy.io;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Factory class for duplicating {@link InputStream}s with ease.
 */
public class InputStreamFactory {
    /**
     * Buffer of data in the stream
     */
    private final byte[] buf;

    /**
     * Constructs a new factory for given stream.
     * @param source stream to make a factory for
     * @throws IOException if copying the stream failed
     */
    public InputStreamFactory(InputStream source) throws IOException {
        buf = source != null ? IOUtils.toByteArray(source) : null;
    }

    /**
     * Returns a new copy of this factory's stream.
     * @return copied input stream
     */
    public InputStream get() {
        return buf != null ? new ByteArrayInputStream(buf) : null;
    }
}
