package ee.shy.core.merge;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
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
public class InputStreamMergerTest extends MergerTest<InputStream> {
    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"numbers"}
        });
    }

    private final String subpackage;

    private InputStreamMerger merger;

    public InputStreamMergerTest(String subpackage) {
        this.subpackage = subpackage;
    }

    @Override
    protected InputStream getState(String name) {
        return getClass().getResourceAsStream(subpackage + "/" + name);
    }

    @Before
    public void setUp() throws Exception {
        merger = new InputStreamMerger();

        super.setUp();
    }

    @Override
    protected void testMerge(InputStream patchable, InputStream original, InputStream revised, InputStream patched) throws IOException {
        Path path = temporaryDirectory.newFile();
        Files.delete(path);
        Files.copy(patchable, path);

        merger.merge(path, original, revised);

        List<String> actualLines = Files.readAllLines(path);
        List<String> expectedLines = IOUtils.readLines(patched);
        assertEquals(expectedLines, actualLines);
    }
}