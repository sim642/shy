package shy.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileAccessor {
    void add(File file, InputStream source) throws IOException;
    InputStream get(File file) throws IOException;
}
