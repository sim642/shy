package ee.shy.storage.accessor;

import ee.shy.storage.FileAccessor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * File accessor to store data plainly as-is.
 * No extension is used on base paths.
 */
public class PlainFileAccessor implements FileAccessor {
    @Override
    public void add(Path path, InputStream source) throws IOException {
        try (OutputStream target = Files.newOutputStream(path)) {
            IOUtils.copy(source, target);
        }
    }

    @Override
    public InputStream get(Path path) throws IOException {
        try {
            return Files.newInputStream(path);
        }
        catch (NoSuchFileException e) { // TODO: 6.04.16 don't catch exception
            return null;
        }
    }
}
