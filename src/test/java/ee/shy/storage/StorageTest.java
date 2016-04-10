package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import ee.shy.io.PathUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageTest {
    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    @Test
    public void testRetrieve() throws IOException {
        Path storageDirectory = temporaryDirectory.newDirectory("storage");
        DataStorage storage = new FileStorage(
                Arrays.asList(
                        new GitFileLocator(storageDirectory),
                        new FlatFileLocator(storageDirectory)),
                new AggregateFileAccessor(Arrays.asList(
                        new GzipFileAccessor(),
                        new PlainFileAccessor())));

        Hash hash1 = storage.put(IOUtils.toInputStream("Hello, World!"));
        assertEquals("Hello, World!", IOUtils.toString(storage.get(hash1)));

        Hash hash2 = storage.put(getClass().getResourceAsStream("/fox.txt"));
        assertEquals(new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"), hash2);
    }

    @Test
    public void testFlatLocator() throws IOException {
        Path storageDirectory = temporaryDirectory.newDirectory("storage");
        FileLocator locator = new FlatFileLocator(storageDirectory);

        assertEquals(storageDirectory.resolve("0000000000000000000000000000000000000000"), locator.locate(Hash.ZERO));
    }

    @Test
    public void testGitLocator() throws IOException {
        Path storageDirectory = temporaryDirectory.newDirectory("storage");
        FileLocator locator = new GitFileLocator(storageDirectory);

        assertEquals(storageDirectory.resolve("00").resolve("00000000000000000000000000000000000000"), locator.locate(Hash.ZERO));
    }

    @Test
    public void testPlainAccessor() throws IOException {
        FileAccessor accessor = new PlainFileAccessor();

        Path file = temporaryDirectory.newFile();
        accessor.add(file, IOUtils.toInputStream("Hello, World!"));
        assertTrue(Files.isRegularFile(file));
        assertEquals("Hello, World!", IOUtils.toString(accessor.get(file)));
    }

    @Test
    public void testGzipAccessor() throws IOException {
        FileAccessor accessor = new GzipFileAccessor();

        Path file = temporaryDirectory.newFile();
        accessor.add(file, IOUtils.toInputStream("Hello, World!"));
        Path extendedFile = PathUtils.addExtension(file, ".gz");
        assertTrue(Files.isRegularFile(extendedFile));
        assertEquals("Hello, World!", IOUtils.toString(accessor.get(file)));
    }

    @Test
    public void testAggregateAccessor() throws IOException {
        FileAccessor writeAccessor = new PlainFileAccessor();

        Path file = temporaryDirectory.newFile();
        writeAccessor.add(file, IOUtils.toInputStream("Hello, World!"));

        FileAccessor readAccessor = new AggregateFileAccessor(Arrays.asList(
                new GzipFileAccessor(),
                new PlainFileAccessor()
        ));

        assertEquals("Hello, World!", IOUtils.toString(readAccessor.get(file)));
    }
}
