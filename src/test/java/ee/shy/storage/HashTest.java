package ee.shy.storage;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalSize() {
        new Hash("1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalParity() {
        new Hash("123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalHexChar() {
        new Hash("foo");
    }

    @Test
    public void testZero() {
        assertEquals(new Hash("0000000000000000000000000000000000000000"), Hash.ZERO);
    }

    @Test
    public void testToString() {
        assertEquals("0000000000000000000000000000000000000000", Hash.ZERO.toString());
        assertEquals("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12", new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12").toString());
    }

    @Test
    public void testBytes() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update("The quick brown fox jumps over the lazy dog".getBytes());

        assertEquals(new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"), new Hash(md.digest()));
    }

    @Test
    public void testEqualsHashcode() {
        Hash hash = new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12");

        assertEquals(hash, hash);
        assertEquals(hash, new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"));
        assertNotEquals(hash, Hash.ZERO);
        assertNotEquals(hash, new Object());

        assertEquals(hash.hashCode(), new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12").hashCode());
    }
}
