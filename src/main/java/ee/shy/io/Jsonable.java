package ee.shy.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An abstract class that JSON writable classes should extend.
 */
public abstract class Jsonable {
    public void write(OutputStream os) throws IOException {
        Json.write(os, this);
    }
}
