package ee.shy.core.merge;

import ee.shy.ResourcePaths;
import ee.shy.core.PathTreeVisitor;
import ee.shy.core.Tree;
import ee.shy.storage.DataStorage;
import ee.shy.storage.MapStorage;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TreeMergerTest extends MergerTest<Tree> {
    @ClassRule
    public static ResourcePaths resourcePaths = new ResourcePaths(TreeMergerTest.class);

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"numbers2"}
        });
    }

    private final Path subpackage;

    private DataStorage storage;
    private TreeMerger merger;

    public TreeMergerTest(String subpackage) {
        this.subpackage = resourcePaths.get(subpackage);
    }

    @Before
    public void setUp() throws Exception {
        storage = new MapStorage();

        patchable = getSubpackageTree("patchable");
        original = getSubpackageTree("original");
        revised = getSubpackageTree("revised");
        patched = getSubpackageTree("patched");

        merger = new TreeMerger(storage);
    }

    private Tree getSubpackageTree(String name) throws IOException {
        return new Tree.Builder(storage).fromDirectory(subpackage.resolve(name)).create();
    }

    @Override
    protected void testMerge(Tree patchable, Tree original, Tree revised, Tree patched) throws IOException {
        Path path = temporaryDirectory.newDirectory();
        patchable.toDirectory(path, storage);

        merger.merge(path, original, revised);

        patched.walk(storage, new PathTreeVisitor(path) {
            @Override
            protected void preVisitTree(Path directory) throws IOException {
                assertTrue(Files.isDirectory(directory));
            }

            @Override
            protected void visitFile(Path file, InputStream is) throws IOException {
                assertTrue(Files.isRegularFile(file));
                try (InputStream fileIs = Files.newInputStream(file)) {
                    assertTrue(IOUtils.contentEquals(is, fileIs));
                }
            }
        });
    }
}