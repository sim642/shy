package ee.shy.io;

import com.google.gson.annotations.SerializedName;
import ee.shy.TemporaryDirectory;
import ee.shy.TestUtils;
import ee.shy.storage.Hash;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class JsonTest {
    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    private TestJsonable testJsonable;

    @Before
    public void setUp() throws Exception {
        testJsonable = new TestJsonable("foo", Hash.ZERO, OffsetDateTime.of(2016, 4, 15, 20, 19, 0, 0, ZoneOffset.ofHours(2)));
    }

    @Test
    public void testUtilsClass() throws Exception {
        TestUtils.assertUtilityClass(Json.class);
    }

    @Test
    public void testRead() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("testRead.json")) {
            TestJsonable readJsonable = Json.read(is, TestJsonable.class);
            assertEquals(testJsonable, readJsonable);
        }
    }

    @Test(expected = IllegalJsonException.class)
    public void testRequired() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("testRequired.json")) {
            Json.read(is, TestJsonable.class);
        }
    }

    @Test
    public void testRetrieve() throws Exception {
        Path file = temporaryDirectory.newFile();

        try (OutputStream os = Files.newOutputStream(file)) {
            testJsonable.write(os);
        }

        try (InputStream is = Files.newInputStream(file)) {
            TestJsonable readJsonable = Json.read(is, TestJsonable.class);
            assertEquals(testJsonable, readJsonable);
        }
    }

    private static class TestJsonable implements Jsonable {
        private final String string;

        @Required
        private final Hash hash;

        @SerializedName("time")
        private final OffsetDateTime offsetDateTime;

        public TestJsonable(String string, Hash hash, OffsetDateTime offsetDateTime) {
            this.string = string;
            this.hash = hash;
            this.offsetDateTime = offsetDateTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            TestJsonable that = (TestJsonable) o;
            return Objects.equals(string, that.string) &&
                    Objects.equals(hash, that.hash) &&
                    Objects.equals(offsetDateTime, that.offsetDateTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(string, hash, offsetDateTime);
        }

        @Override
        public String toString() {
            return "TestJsonable{" +
                    "string='" + string + '\'' +
                    ", hash=" + hash +
                    ", offsetDateTime=" + offsetDateTime +
                    '}';
        }
    }
}