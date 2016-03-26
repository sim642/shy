package ee.shy.io;

import java.io.*;

/**
 * An abstract class that JSON writable classes should extend.
 */
public abstract class Jsonable {
    /**
     * Writes the JSON object to given output stream.
     * @param os output stream to write to
     * @throws IOException if there was a problem writing to the output stream
     */
    public void write(OutputStream os) throws IOException {
        Json.write(os, this);
    }

    /**
     * Makes an input stream out of the JSON object.
     * @return input stream to read from
     * @throws IOException if there was a problem writing to the piped stream
     */
    public InputStream inputify() throws IOException {
        PipedOutputStream os = new PipedOutputStream();
        PipedInputStream is = new PipedInputStream(os);

        this.write(os);
        return is;
    }
}
