package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class DataStorageTest {
    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    protected DataStorage storage;

    @Test
    public void testRetrieveString() throws Exception {
        Hash hash = storage.put(IOUtils.toInputStream("Hello, World!"));
        assertEquals("Hello, World!", IOUtils.toString(storage.get(hash)));
    }

    @Test
    public void testRetrieveFile() throws Exception {
        Hash hash = storage.put(getClass().getResourceAsStream("/fox.txt"));
        assertEquals(new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"), hash);
    }

    @Test
    public void testNonExistent() throws Exception {
        assertNull(storage.get(Hash.ZERO));
    }
}