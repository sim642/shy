package ee.shy.io;

import ee.shy.TemporaryDirectory;
import ee.shy.TestUtils;
import ee.shy.storage.Hash;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

public class JsonTest {
    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    @Test
    public void testUtilsClass() throws Exception {
        TestUtils.assertUtilityClass(Json.class);
    }

    @Test
    public void testRead() throws Exception {
        TestJsonable testJsonable = new TestJsonable("foo", Hash.ZERO, OffsetDateTime.of(2016, 4, 15, 20, 19, 0, 0, ZoneOffset.ofHours(2)));

        try (InputStream is = getClass().getResourceAsStream("testjsonable.json")) {
            TestJsonable readJsonable = Json.read(is, TestJsonable.class);
            assertEquals(testJsonable, readJsonable);
        }
    }

    @Test(expected = IllegalJsonException.class)
    public void testRequired() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("testjsonable-required.json")) {
            Json.read(is, TestJsonable.class);
        }
    }

    @Test(expected = IllegalJsonException.class)
    public void testIllegal() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("testjsonable-illegal.json")) {
            Json.read(is, TestJsonable.class);
        }
    }

    @Test
    public void testRetrieve() throws Exception {
        TestJsonable testJsonable = TestJsonable.newRandom();
        Path file = temporaryDirectory.newFile();

        try (OutputStream os = Files.newOutputStream(file)) {
            testJsonable.write(os);
        }

        try (InputStream is = Files.newInputStream(file)) {
            TestJsonable readJsonable = Json.read(is, TestJsonable.class);
            assertEquals(testJsonable, readJsonable);
        }
    }

}