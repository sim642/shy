package shy.storage;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipFileAccessor implements FileAccessor {
    @Override
    public void add(File file, InputStream source) throws IOException {
        try (GZIPOutputStream target = new GZIPOutputStream(new FileOutputStream(addExtension(file)))) {
            Util.copyStream(source, target);
        }
    }

    @Override
    public InputStream get(File file) throws IOException {
        try {
            return new GZIPInputStream(new FileInputStream(addExtension(file)));
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    private static File addExtension(File file) {
        return new File(file.getAbsolutePath() + ".gz");
    }
}
