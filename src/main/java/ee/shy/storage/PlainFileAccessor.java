package ee.shy.storage;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * File accessor to store data plainly as-is.
 * No extension is used on base paths.
 */
public class PlainFileAccessor implements FileAccessor {
    @Override
    public void add(File file, InputStream source) throws IOException {
        try (FileOutputStream target = new FileOutputStream(file)) {
            IOUtils.copy(source, target);
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
