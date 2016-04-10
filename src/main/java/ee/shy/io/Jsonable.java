package ee.shy.io;

import java.io.*;

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
}
