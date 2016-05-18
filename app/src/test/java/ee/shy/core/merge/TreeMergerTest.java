package ee.shy.core.merge;

import ee.shy.ResourcePaths;
import ee.shy.TemporaryDirectory;
import ee.shy.core.PathTreeVisitor;
import ee.shy.core.Tree;
import ee.shy.storage.DataStorage;
import ee.shy.storage.MapStorage;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.ClassRule;
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

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TreeMergerTest {
    @ClassRule
    public static ResourcePaths resourcePaths = new ResourcePaths(TreeMergerTest.class);

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"numbers2"}
        });
    }

    @Rule
    public TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private final Path subpackage;

    private DataStorage storage;
    private TreeMerger merger;
    private Tree patchable;
    private Tree original;
    private Tree revised;
    private Tree patched;

    public TreeMergerTest(String subpackage) {
        this.subpackage = resourcePaths.get(subpackage);
    }

    @Before
    public void setUp() throws Exception {
        storage = new MapStorage();

        patchable = new Tree.Builder(storage).fromDirectory(subpackage.resolve("patchable")).create();
        original = new Tree.Builder(storage).fromDirectory(subpackage.resolve("original")).create();
        revised = new Tree.Builder(storage).fromDirectory(subpackage.resolve("revised")).create();
        patched = new Tree.Builder(storage).fromDirectory(subpackage.resolve("patched")).create();

        merger = new TreeMerger(storage);
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

    private void testMerge(Tree patchable, Tree original, Tree revised, Tree patched) throws IOException {
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