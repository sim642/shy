package ee.shy.storage;

import org.junit.Before;

public class MapStorageTest extends DataStorageTest {
    @Before
    public void setUp() throws Exception {
        storage = new MapStorage();
    }
}