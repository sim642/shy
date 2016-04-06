package ee.shy.storage;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static final String EXTENSION = ".gz"; // TODO: 9.03.16 create superclass for extension handling

    @Override
    public void add(Path path, InputStream source) throws IOException {
        try (GZIPOutputStream target = new GZIPOutputStream(Files.newOutputStream(Util.addExtension(path, EXTENSION)))) {
            IOUtils.copy(source, target);
        }
    }

    @Override
    public InputStream get(Path path) throws IOException {
        try {
            return new GZIPInputStream(Files.newInputStream(Util.addExtension(path, EXTENSION)));
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }
}
