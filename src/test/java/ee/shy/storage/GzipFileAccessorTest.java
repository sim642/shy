package ee.shy.storage;

import ee.shy.io.PathUtils;
import org.junit.Before;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GzipFileAccessorTest extends FileAccessorTest {
    @Before
    @Override
    public void setUp() throws Exception {
        accessor = new GzipFileAccessor();
        super.setUp();
    }

    @Override
    protected void checkFile(Path path) {
        Path extendedPath = PathUtils.addExtension(path, ".gz");
        assertTrue(Files.isRegularFile(extendedPath));

        assertFalse(Files.exists(path));
    }
}