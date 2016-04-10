package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

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

        assertEquals(
                storageDirectory
                        .resolve("0000000000000000000000000000000000000000"),
                locator.locate(Hash.ZERO));
    }

    @Test
    public void testNestedLocator() throws IOException {
        FileLocator locator = new NestedFileLocator(storageDirectory, 10);

        assertEquals(
                storageDirectory
                        .resolve("0000000000")
                        .resolve("0000000000")
                        .resolve("0000000000")
                        .resolve("0000000000"),
                locator.locate(Hash.ZERO));
    }

    @Test
    public void testGitLocator() throws IOException {
        FileLocator locator = new GitFileLocator(storageDirectory);

        assertEquals(
                storageDirectory
                        .resolve("00")
                        .resolve("00000000000000000000000000000000000000"),
                locator.locate(Hash.ZERO));
    }
}
