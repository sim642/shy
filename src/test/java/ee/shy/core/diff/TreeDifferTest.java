package ee.shy.core.diff;

import ee.shy.ResourcePaths;
import ee.shy.core.Tree;
import ee.shy.storage.DataStorage;
import ee.shy.storage.MapStorage;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNotNull;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TreeDifferTest {
    @ClassRule
    public static ResourcePaths resourcePaths = new ResourcePaths(TreeDifferTest.class);

    @Parameters(name = "{0}-{1}")
    public static Collection<Object[]> parameters() throws IOException, URISyntaxException {
        /*
            Cannot use ClassRule in Parameters due to:
            - https://github.com/junit-team/junit4/issues/671,
            - https://github.com/junit-team/junit4/issues/527.
         */
        String[] trees = Files.list(ResourcePaths.getPath(TreeDifferTest.class, ""))
                .filter(path -> Files.isDirectory(path))
                .map(path -> path.getFileName().toString())
                .toArray(String[]::new);

        List<Object[]> parameters = new ArrayList<>();
        for (String original : trees) {
            for (String revised : trees) {
                parameters.add(new Object[]{original, revised});
            }
        }
        return parameters;
    }

    private DataStorage storage;
    private TreeDiffer treeDiffer;

    private final String original;
    private final String revised;

    public TreeDifferTest(String original, String revised) {
        this.original = original;
        this.revised = revised;
    }

    @Before
    public void setUp() throws Exception {
        storage = new MapStorage();
        treeDiffer = new TreeDiffer(storage);
    }

    @Test
    public void testDiff() throws Exception {
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