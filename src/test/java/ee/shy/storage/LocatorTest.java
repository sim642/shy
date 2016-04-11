package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class LocatorTest {
    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private Path storageDirectory;

    @Before
    public void setUp() throws Exception {
        storageDirectory = temporaryDirectory.newDirectory("storage");
    }

    @Test
    public void testFlatLocator() throws IOException {
        FileLocator locator = new FlatFileLocator(storageDirectory);

        Path path = storageDirectory
                .resolve("0000000000000000000000000000000000000000");

        assertEquals(path, locator.locateAdd(Hash.ZERO));
        assertEquals(path, locator.locateGet(Hash.ZERO));
    }

    @Test
    public void testNestedLocator() throws IOException {
        FileLocator locator = new NestedFileLocator(storageDirectory, 10);

        Path path = storageDirectory
                .resolve("0000000000")
                .resolve("0000000000")
                .resolve("0000000000")
                .resolve("0000000000");

        assertEquals(path, locator.locateAdd(Hash.ZERO));
        assertTrue(Files.isDirectory(path.getParent()));
        assertEquals(path, locator.locateGet(Hash.ZERO));
    }

    @Test
    public void testGitLocator() throws IOException {
        FileLocator locator = new GitFileLocator(storageDirectory);

        Path path = storageDirectory
                .resolve("00")
                .resolve("00000000000000000000000000000000000000");

        assertEquals(path, locator.locateAdd(Hash.ZERO));
        assertTrue(Files.isDirectory(path.getParent()));
        assertEquals(path, locator.locateGet(Hash.ZERO));
    }

    @Test
    public void testAbstractLocator() throws IOException {
        FileLocator locator = new AbstractFileLocator(storageDirectory);

        assertNull(locator.locateAdd(Hash.ZERO));
        assertNull(locator.locateGet(Hash.ZERO));
    }

    private class AbstractFileLocator extends FileLocator {
        public AbstractFileLocator(Path root) throws IOException {
            super(root);
        }
    }
}
