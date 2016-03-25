package ee.shy.io;

import java.io.IOException;
import java.io.OutputStream;

public class Jsonable {
    public void write(OutputStream os) throws IOException {
        Json.write(os, this);
    }
}
