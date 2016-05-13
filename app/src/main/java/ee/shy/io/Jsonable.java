package ee.shy.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Interface that JSON writable classes should implement.
 */
public interface Jsonable {
    /**
     * Writes the JSON object to given output stream.
     * @param os output stream to write to
     * @throws IOException if there was a problem writing to the output stream
     */
    default void write(OutputStream os) throws IOException {
        Json.write(os, this);
    }

    /**
     * Writes the JSON object to given file.
     * @param path file to write to
     * @throws IOException if there was a problem writing to the file
     */
    default void write(Path path) throws IOException {
        Json.write(path, this);
    }
}
