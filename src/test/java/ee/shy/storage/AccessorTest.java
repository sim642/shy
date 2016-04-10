package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import ee.shy.io.PathUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AccessorTest {
    private static final String TEST_STRING = "Hello, World!";

    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private Path file;

    @Before
    public void setUp() throws Exception {
        file = temporaryDirectory.newFile();
    }

    @Test
    public void testPlainAccessor() throws IOException {
        FileAccessor accessor = new PlainFileAccessor();

        accessor.add(file, IOUtils.toInputStream(TEST_STRING));
        assertTrue(Files.isRegularFile(file));
        assertEquals(TEST_STRING, IOUtils.toString(accessor.get(file)));

        assertNull(accessor.get(Paths.get("non-existent")));
    }

    @Test
    public void testGzipAccessor() throws IOException {
        FileAccessor accessor = new GzipFileAccessor();

        accessor.add(file, IOUtils.toInputStream(TEST_STRING));
        Path extendedFile = PathUtils.addExtension(file, ".gz");
        assertTrue(Files.isRegularFile(extendedFile));
        assertEquals(TEST_STRING, IOUtils.toString(accessor.get(file)));

        assertNull(accessor.get(Paths.get("non-existent")));
    }

    @Test
    public void testAggregateAccessor() throws IOException {
        FileAccessor writeAccessor = new PlainFileAccessor();

        writeAccessor.add(file, IOUtils.toInputStream(TEST_STRING));

        FileAccessor readAccessor = new AggregateFileAccessor(Arrays.asList(
                new GzipFileAccessor(),
                new PlainFileAccessor()
        ));

        assertEquals(TEST_STRING, IOUtils.toString(readAccessor.get(file)));

        assertNull(readAccessor.get(Paths.get("non-existent")));
    }
}
