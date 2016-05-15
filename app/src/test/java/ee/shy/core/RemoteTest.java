package ee.shy.core;

import ee.shy.io.IllegalJsonException;
import ee.shy.io.Json;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class RemoteTest {
    @Test(expected = NullPointerException.class)
    public void testConstructorIllegalURI() throws Exception {
        new Remote(null);
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalURI() throws Exception {
        Json.read(getClass().getResourceAsStream("remote-illegal-uri.json"), Remote.class);
    }

    @Test
    public void testRead() throws Exception {
        Remote remote = Json.read(getClass().getResourceAsStream("remote.json"), Remote.class);
        assertEquals(URI.create("ssh://shy@example.com/home/shy/test"), remote.getURI());
    }
}