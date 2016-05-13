package ee.shy.io;

import com.google.gson.annotations.SerializedName;
import ee.shy.storage.Hash;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.OffsetDateTime;
import java.util.Objects;

public class TestJsonable implements Jsonable, Validated {
    private final String string;

    private final Hash hash;

    @SerializedName("time")
    private final OffsetDateTime offsetDateTime;

    public TestJsonable(String string, Hash hash, OffsetDateTime offsetDateTime) {
        this.string = string;
        this.hash = hash;
        this.offsetDateTime = offsetDateTime;
    }

    public static TestJsonable newRandom() {
        return new TestJsonable(
                RandomStringUtils.randomAscii(20),
                new Hash(RandomStringUtils.random(40, "0123456789abcdef".toCharArray())),
                OffsetDateTime.now()
        );
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

    @Override
    public void assertValid() {
        Objects.requireNonNull(hash);
    }
}
