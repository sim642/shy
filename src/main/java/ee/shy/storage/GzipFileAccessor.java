package ee.shy.storage;

import org.apache.commons.io.IOUtils;

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
    protected static final String EXTENSION = ".gz"; // TODO: 9.03.16 create superclass for extension handling

    @Override
    public void add(File file, InputStream source) throws IOException {
        try (GZIPOutputStream target = new GZIPOutputStream(new FileOutputStream(Util.addExtension(file, EXTENSION)))) {
            IOUtils.copy(source, target);
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
