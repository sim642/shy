package ee.shy.io;

import ee.shy.TemporaryDirectory;
import ee.shy.TestUtils;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class PathUtilsTest {
    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    @Test
    public void testUtilsClass() throws Exception {
        TestUtils.assertUtilityClass(PathUtils.class);
    }

    @Test
    public void testAddExtension() throws Exception {
        assertEquals(Paths.get("foo.txt"), PathUtils.addExtension(Paths.get("foo"), ".txt"));
        assertEquals(Paths.get("foo", "bar.txt"), PathUtils.addExtension(Paths.get("foo", "bar"), ".txt"));
    }

    @Test
    public void testCreateParentDirectoriesNew() throws Exception {
        Path path = temporaryDirectory.getRoot().resolve(Paths.get("foo", "bar", "baz"));
        PathUtils.createParentDirectories(path);

        assertTrue(Files.isDirectory(temporaryDirectory.getRoot().resolve(Paths.get("foo"))));
        assertTrue(Files.isDirectory(temporaryDirectory.getRoot().resolve(Paths.get("foo", "bar"))));
        assertFalse(Files.exists(temporaryDirectory.getRoot().resolve(Paths.get("foo", "bar", "baz"))));
    }

    @Test
    public void testCreateParentDirectoriesExisting() throws Exception {
        Path directoryPath = Paths.get("foo", "bar");
        temporaryDirectory.newDirectory(directoryPath);

        PathUtils.createParentDirectories(temporaryDirectory.getRoot().resolve(directoryPath));

        Path filePath = Paths.get("qwe", "rty");
        temporaryDirectory.newFile(filePath);
        PathUtils.createParentDirectories(temporaryDirectory.getRoot().resolve(filePath));
    }
}