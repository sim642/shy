package ee.shy.storage.accessor;

import ee.shy.io.PathUtils;
import ee.shy.storage.FileAccessor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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
        try (OutputStream outputStream = Files.newOutputStream(PathUtils.addExtension(path, EXTENSION));
             GZIPOutputStream target = new GZIPOutputStream(outputStream)) {
            IOUtils.copy(source, target);
        }
    }

    @Override
    public InputStream get(Path path) throws IOException {
        try {
            return new GZIPInputStream(Files.newInputStream(PathUtils.addExtension(path, EXTENSION)));
        }
        catch (NoSuchFileException e) {
            return null;
        }
    }
}
