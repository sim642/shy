package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class FileAccessorTest {
    protected static final String TEST_STRING = "Hello, World!";

    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    protected FileAccessor accessor;
    protected Path file;

    protected void checkFile(Path path) {

    }

    @Before
    public void setUp() throws Exception {
        file = temporaryDirectory.newFile();
        Files.delete(file); // TODO: 11.04.16 create temporary path without creating file
    }

    @Test
    public void testRetrieve() throws Exception {
        accessor.add(file, IOUtils.toInputStream(TEST_STRING));
        checkFile(file);
        assertEquals(TEST_STRING, IOUtils.toString(accessor.get(file)));
    }

    @Test
    public void testNonExistent() throws Exception {
        assertNull(accessor.get(file));
    }
}