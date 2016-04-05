import ee.shy.storage.Hash;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

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
        assertEquals("0000000000000000000000000000000000000000", Hash.ZERO.toString());
    }

    @Test
    public void testString() {
        assertEquals("0000000000000000000000000000000000000000", new Hash("0000000000000000000000000000000000000000").toString());
        assertEquals("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12", new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12").toString());
    }

    @Test
    public void testMessageDigest() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update("The quick brown fox jumps over the lazy dog".getBytes());

        assertEquals("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12", new Hash(md).toString());
    }

    @Test
    public void testEqualsHashcode() {
        assertEquals(new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"), new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"));
        assertEquals(new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12").hashCode(), new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12").hashCode());
    }
}
