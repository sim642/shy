package ee.shy.storage;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AggregateFileAccessorTest extends FileAccessorTest {
    private PlainFileAccessor plainFileAccessor;
    private GzipFileAccessor gzipFileAccessor;

    @Before
    @Override
    public void setUp() throws Exception {
        plainFileAccessor = new PlainFileAccessor();
        gzipFileAccessor = new GzipFileAccessor();
        accessor = new AggregateFileAccessor(Arrays.asList(
                gzipFileAccessor,
                plainFileAccessor
        ));

        super.setUp();
    }

    @Test
    public void testCrossGet() throws Exception {
        plainFileAccessor.add(file, IOUtils.toInputStream(TEST_STRING));

        assertEquals(TEST_STRING, IOUtils.toString(accessor.get(file)));
    }

    @Test
    public void testCrossAdd() throws Exception {
        accessor.add(file, IOUtils.toInputStream(TEST_STRING));

        assertEquals(TEST_STRING, IOUtils.toString(gzipFileAccessor.get(file)));
        assertNull(plainFileAccessor.get(file));
    }
}