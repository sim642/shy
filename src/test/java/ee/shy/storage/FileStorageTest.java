package ee.shy.storage;

import org.junit.Before;

import java.nio.file.Path;
import java.util.Arrays;

public class FileStorageTest extends DataStorageTest {
    private Path storageDirectory;

    @Before
    public void setUp() throws Exception {
        storageDirectory = temporaryDirectory.newDirectory("storage");
        storage = new FileStorage(
                Arrays.asList(
                        new GitFileLocator(storageDirectory),
                        new FlatFileLocator(storageDirectory)),
                new AggregateFileAccessor(Arrays.asList(
                        new GzipFileAccessor(),
                        new PlainFileAccessor())));
    }
}