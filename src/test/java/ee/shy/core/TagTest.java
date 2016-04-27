package ee.shy.core;

import ee.shy.io.IllegalJsonException;
import ee.shy.io.Json;
import ee.shy.storage.Hash;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalHash() throws Exception {
        new Tag(null, "");
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalHash() throws Exception {
        Json.read(getClass().getResourceAsStream("tag-illegal-hash.json"), Tag.class);
    }

    @Test
    public void testRead() throws Exception {
        Tag tag = Json.read(getClass().getResourceAsStream("tag.json"), Tag.class);
        assertEquals(Hash.ZERO, tag.getHash());
        assertEquals("foo", tag.getMessage());
    }
}