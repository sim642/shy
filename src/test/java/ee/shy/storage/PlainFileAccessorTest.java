package ee.shy.storage;

import org.junit.Before;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class PlainFileAccessorTest extends FileAccessorTest {
    @Before
    @Override
    public void setUp() throws Exception {
        accessor = new PlainFileAccessor();
        super.setUp();
    }

    @Override
    protected void checkFile(Path path) {
        assertTrue(Files.isRegularFile(path));
    }
}