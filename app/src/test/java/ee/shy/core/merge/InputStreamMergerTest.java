package ee.shy.core.merge;

import ee.shy.TemporaryDirectory;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class InputStreamMergerTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"foo/patchable", "foo/original", "foo/revised", "foo/patched"}
        });
    }

    @Rule
    public TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private final String patchableResource;
    private final String originalResource;
    private final String revisedResource;
    private final String patchedResource;

    private InputStream patchable;
    private InputStream original;
    private InputStream revised;
    private InputStream patched;
    private InputStreamMerger merger;

    public InputStreamMergerTest(String patchableResource, String originalResource, String revisedResource, String patchedResource) {
        this.patchableResource = patchableResource;
        this.originalResource = originalResource;
        this.revisedResource = revisedResource;
        this.patchedResource = patchedResource;
    }

    @Before
    public void setUp() throws Exception {
        patchable = getClass().getResourceAsStream(patchableResource);
        original = getClass().getResourceAsStream(originalResource);
        revised = getClass().getResourceAsStream(revisedResource);
        patched = getClass().getResourceAsStream(patchedResource);
        merger = new InputStreamMerger();
    }

    @Test
    public void testForwardMerge() throws Exception {
        Path path = temporaryDirectory.newFile();
        Files.delete(path);
        Files.copy(patchable, path);

        merger.merge(path, original, revised);

        List<String> actualLines = Files.readAllLines(path);
        List<String> expectedLines = IOUtils.readLines(patched);
        assertEquals(expectedLines, actualLines);
    }

    @Test
    public void testBackwardMerge() throws Exception {
        Path path = temporaryDirectory.newFile();
        Files.delete(path);
        Files.copy(patched, path);

        merger.merge(path, revised, original);

        List<String> actualLines = Files.readAllLines(path);
        List<String> expectedLines = IOUtils.readLines(patchable);
        assertEquals(expectedLines, actualLines);
    }
}