package ee.shy.map;

import ee.shy.TemporaryDirectory;
import ee.shy.io.Json;
import ee.shy.io.TestJsonable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class DirectoryJsonMapTest {
    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private Path directory;
    private DirectoryJsonMap<TestJsonable> jsonMap;
    private TestJsonable testJsonable;

    @Before
    public void setUp() throws Exception {
        directory = temporaryDirectory.newDirectory();
        jsonMap = new DirectoryJsonMap<>(TestJsonable.class, directory);
        testJsonable = TestJsonable.newRandom();
    }

    @Test
    public void testPut() throws Exception {
        jsonMap.put("foo", testJsonable);

        Path path = directory.resolve("foo");
        try (InputStream is = Files.newInputStream(path)) {
            TestJsonable readJsonable = Json.read(is, TestJsonable.class);
            assertEquals(testJsonable, readJsonable);
        }
    }

    @Test
    public void testRetrieve() throws Exception {
        jsonMap.put("foo", testJsonable);

        assertEquals(testJsonable, jsonMap.get("foo"));
        assertNull(jsonMap.get("bar"));
    }

    @Test
    public void testRemove() throws Exception {
        jsonMap.put("foo", testJsonable);
        jsonMap.remove("foo");

        assertFalse(Files.exists(directory.resolve("foo")));
    }

    @Test
    public void testKeySet() throws Exception {
        HashSet<String> keySet = new HashSet<>(Arrays.asList("foo", "bar", "baz"));

        for (String key : keySet)
            jsonMap.put(key, testJsonable);

        assertEquals(keySet, jsonMap.keySet());
    }
}