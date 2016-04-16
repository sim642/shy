package ee.shy.core.diff;

import ee.shy.ResourcePaths;
import ee.shy.core.Tree;
import ee.shy.storage.DataStorage;
import ee.shy.storage.MapStorage;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNotNull;

@RunWith(Theories.class)
public class TreeDifferTest {
    @Rule
    public ResourcePaths resourcePaths = new ResourcePaths(TreeDifferTest.class);

    private DataStorage storage;
    private TreeDiffer treeDiffer;

    @DataPoints("trees")
    public static String[] listTrees() throws URISyntaxException, IOException {
        return Files.list(ResourcePaths.getPath(TreeDifferTest.class, "")).filter(path -> Files.isDirectory(path)).map(path -> path.getFileName().toString()).toArray(String[]::new);
    }

    @Before
    public void setUp() throws Exception {
        storage = new MapStorage();
        treeDiffer = new TreeDiffer(storage);
    }

    @Theory
    public void testDiff(
            @FromDataPoints("trees") String original,
            @FromDataPoints("trees") String revised
    ) throws Exception {
        System.out.println(original + "-" + revised);
        List<String> expectedLines;
        if (original.equals(revised)) {
            expectedLines = Collections.emptyList();
        }
        else {
            InputStream expectedStream = getClass().getResourceAsStream(original + "-" + revised);
            assumeNotNull(expectedStream);
            expectedLines = IOUtils.readLines(expectedStream);
        }

        Tree originalTree = new Tree.Builder(storage).fromDirectory(resourcePaths.get(original)).create();
        Tree revisedTree = new Tree.Builder(storage).fromDirectory(resourcePaths.get(revised)).create();
        List<String> actualLines = treeDiffer.diff(originalTree, revisedTree);

        assertEquals(expectedLines, actualLines);
    }
}