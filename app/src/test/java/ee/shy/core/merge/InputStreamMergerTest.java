package ee.shy.core.merge;

import ee.shy.TemporaryDirectory;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class InputStreamMergerTest {
    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"numbers"}
        });
    }

    @Rule
    public TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private final String subpackage;

    private InputStream patchable;
    private InputStream original;
    private InputStream revised;
    private InputStream patched;
    private InputStreamMerger merger;

    public InputStreamMergerTest(String subpackage) {
        this.subpackage = subpackage;
    }

    @Before
    public void setUp() throws Exception {
        patchable = getClass().getResourceAsStream(subpackage + "/patchable");
        original = getClass().getResourceAsStream(subpackage + "/original");
        revised = getClass().getResourceAsStream(subpackage + "/revised");
        patched = getClass().getResourceAsStream(subpackage + "/patched");
        merger = new InputStreamMerger();
    }

    @Test
    public void testForwardMainMerge() throws Exception {
        testMerge(patchable, original, revised, patched);
    }

    @Test
    public void testForwardSideMerge() throws Exception {
        testMerge(revised, original, patchable, patched);
    }

    @Test
    public void testBackwardMainMerge() throws Exception {
        testMerge(patched, revised, original, patchable);
    }

    @Test
    public void testBackwardSideMerge() throws Exception {
        testMerge(original, revised, patched, patchable);
    }

    private void testMerge(InputStream patchable, InputStream original, InputStream revised, InputStream patched) throws IOException {
        Path path = temporaryDirectory.newFile();
        Files.delete(path);
        Files.copy(patchable, path);

        merger.merge(path, original, revised);

        List<String> actualLines = Files.readAllLines(path);
        List<String> expectedLines = IOUtils.readLines(patched);
        assertEquals(expectedLines, actualLines);
    }
}