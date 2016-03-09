package shy.storage;

import java.io.*;

public class PlainFileAccessor implements FileAccessor {
    @Override
    public void add(File file, InputStream source) throws IOException {
        try (FileOutputStream target = new FileOutputStream(file)) {
            Util.copyStream(source, target);
        }

    }

    @Override
    public InputStream get(File file) {
        try {
            return new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }
}
