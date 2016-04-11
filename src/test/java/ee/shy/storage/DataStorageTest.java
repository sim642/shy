package ee.shy.storage;

import ee.shy.TemporaryDirectory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class DataStorageTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Rule
    public final TemporaryDirectory temporaryDirectory = new TemporaryDirectory();

    protected DataStorage storage;
    protected Provider provider;

    @Before
    public void setUp() throws Exception {
        provider = MessageDigest.getInstance("SHA-1").getProvider();
    }

    @After
    public void tearDown() throws Exception {
        if (!ArrayUtils.contains(Security.getProviders(), provider)) {
            Security.addProvider(provider);
        }
    }

    @Test
    public void testRetrieveString() throws Exception {
        Hash hash = storage.put(IOUtils.toInputStream("Hello, World!"));
        assertEquals("Hello, World!", IOUtils.toString(storage.get(hash)));
    }

    @Test
    public void testRetrieveFile() throws Exception {
        Hash hash = storage.put(getClass().getResourceAsStream("/fox.txt"));
        assertEquals(new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"), hash);
    }

    @Test
    public void testNonExistent() throws Exception {
        assertNull(storage.get(Hash.ZERO));
    }

    @Test
    public void testPutNoAlgorithm() throws Exception {
        Security.removeProvider(provider.getName());

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(CoreMatchers.isA(NoSuchAlgorithmException.class));
        storage.put(IOUtils.toInputStream("Hello, World!"));
    }

    @Test
    public void testGetNoAlgorithm() throws Exception {
        Hash hash = storage.put(IOUtils.toInputStream("Hello, World!"));

        Security.removeProvider(provider.getName());

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(CoreMatchers.isA(NoSuchAlgorithmException.class));
        storage.get(hash);
    }
}