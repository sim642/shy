package shy.storage;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipFileAccessor implements FileAccessor {
    @Override
    public void add(File file, InputStream source) throws IOException {
        try (GZIPOutputStream target = new GZIPOutputStream(new FileOutputStream(Util.addExtension(file, ".gz")))) {
            Util.copyStream(source, target);
        }
    }

    @Override
    public InputStream get(File file) throws IOException {
        try {
            return new GZIPInputStream(new FileInputStream(Util.addExtension(file, ".gz")));
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

}
