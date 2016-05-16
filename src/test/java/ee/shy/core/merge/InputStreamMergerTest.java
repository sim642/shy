package ee.shy.core.merge;

import ee.shy.core.diff.TreeDifferTest;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class InputStreamMergerTest {

    private InputStreamMerger inputStreamMerger;

    @Before
    public void setUp() throws Exception {
        inputStreamMerger = new InputStreamMerger();
    }

    @Parameters(name = "{0}-{1}-{2}-{3}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                new Object[]{ "foo-original.txt", "foo-edit1.txt", "foo-edit2.txt", "expected-results.txt"}
        });
    }

    private final String original;
    private final String revised1;
    private final String revised2;
    private final String expected;

    public InputStreamMergerTest(String original, String revised1, String revised2, String expected) {
        this.original = original;
        this.revised1 = revised1;
        this.revised2 = revised2;
        this.expected = expected;
    }

    @Test
    public void testInsertMerge() throws Exception {
        InputStream originalInputStream = getClass().getResourceAsStream(original);
        InputStream revisedInputStream1 = getClass().getResourceAsStream(revised1);
        InputStream revisedInputStream2 = getClass().getResourceAsStream(revised2);
        InputStream expectedStream = getClass().getResourceAsStream(expected);
        assertEquals(IOUtils.readLines(expectedStream), IOUtils.readLines(
                inputStreamMerger.merge(originalInputStream, revisedInputStream1, revisedInputStream2)
        ));
    }

    @Test
    public void testDeleteMerge() throws Exception {
        InputStream originalInputStream = getClass().getResourceAsStream(expected);
        InputStream revisedInputStream1 = getClass().getResourceAsStream(revised2);
        InputStream revisedInputStream2 = getClass().getResourceAsStream(revised1);
        InputStream expectedStream = getClass().getResourceAsStream(original);
        assertEquals(IOUtils.readLines(expectedStream), IOUtils.readLines(
                inputStreamMerger.merge(originalInputStream, revisedInputStream1, revisedInputStream2)
        ));
    }
}