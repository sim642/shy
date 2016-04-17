package ee.shy.core;

import ee.shy.ResourcePaths;
import ee.shy.TemporaryDirectory;
import ee.shy.storage.DataStorage;
import ee.shy.storage.MapStorage;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class TreeTest {
    @ClassRule
    public static ResourcePaths resourcePaths = new ResourcePaths(TreeTest.class);

    @Rule
    public TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private DataStorage storage;
    private Tree tree;

    @Before
    public void setUp() throws Exception {
        storage = new MapStorage();
        tree = new Tree.Builder(storage).fromDirectory(resourcePaths.get("tree")).create();
    }

    @Test
    public void testReplicate() throws Exception {
        Path directory = temporaryDirectory.newDirectory();
        tree.toDirectory(directory, storage);

        tree.walk(storage, new PathTreeVisitor(directory) {
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

            @Override
            protected void postVisitTree(Path directory) throws IOException {

            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutable() throws Exception {
        tree.getItems().clear();
    }
}