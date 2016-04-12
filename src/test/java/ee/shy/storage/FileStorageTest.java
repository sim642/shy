package ee.shy.storage;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

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
        super.setUp();
    }

    @Test
    public void testDataChange() throws Exception {
        Hash hash = storage.put(getClass().getResourceAsStream("/fox.txt"));

        GitFileLocator gitFileLocator = new GitFileLocator(storageDirectory);
        GzipFileAccessor gzipFileAccessor = new GzipFileAccessor();

        gzipFileAccessor.add(gitFileLocator.locate(hash), IOUtils.toInputStream("Hello, World!"));

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("not");
        expectedException.expectMessage("match");
        storage.get(hash);
    }
}