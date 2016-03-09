package shy.storage;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * File accessor to store data compressed using GZIP.
 * Extension <code>.gz</code> is used on base paths.
 */
public class GzipFileAccessor implements FileAccessor {
    /**
     * Extension used on base paths.
     */
    protected static final String EXTENSION = ".gz";

    @Override
    public void add(File file, InputStream source) throws IOException {
        try (GZIPOutputStream target = new GZIPOutputStream(new FileOutputStream(Util.addExtension(file, EXTENSION)))) {
            Util.copyStream(source, target);
        }
    }

    @Override
    public InputStream get(File file) throws IOException {
        try {
            return new GZIPInputStream(new FileInputStream(Util.addExtension(file, EXTENSION)));
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

}
