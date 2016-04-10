package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

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
}
