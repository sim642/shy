package ee.shy.storage;

import ee.shy.SecurityProviderRecoverer;
import ee.shy.TemporaryDirectory;
import ee.shy.io.TestJsonable;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
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
    @Rule
    public final SecurityProviderRecoverer securityProviderRecoverer = new SecurityProviderRecoverer();

    protected DataStorage storage;
    protected Provider hashProvider;

    @Before
    public void setUp() throws Exception {
        hashProvider = MessageDigest.getInstance("SHA-1").getProvider();
    }

    @Test
    public void testRetrieveString() throws Exception {
        Hash hash = storage.put(IOUtils.toInputStream("Hello, World!"));
        assertEquals("Hello, World!", IOUtils.toString(storage.get(hash)));
    }

    @Test
    public void testRetrieveFile() throws Exception {
        Hash hash = storage.put(getClass().getResourceAsStream("fox.txt"));
        assertEquals(new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12"), hash);
    }

    @Test
    public void testRetrieveJson() throws Exception {
        TestJsonable testJsonable = TestJsonable.newRandom();
        Hash hash = storage.put(testJsonable);
        assertEquals(testJsonable, storage.get(hash, TestJsonable.class));
    }

    @Test
    public void testNonExistent() throws Exception {
        assertNull(storage.get(Hash.ZERO));
    }

    @Test
    public void testNonExistentJson() throws Exception {
        assertNull(storage.get(Hash.ZERO, TestJsonable.class));
    }

    @Test
    public void testPutNoAlgorithm() throws Exception {
        Security.removeProvider(hashProvider.getName());

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(CoreMatchers.isA(NoSuchAlgorithmException.class));
        storage.put(IOUtils.toInputStream("Hello, World!"));
    }

    @Test
    public void testPutJsonNoAlgorithm() throws Exception {
        Security.removeProvider(hashProvider.getName());

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(CoreMatchers.isA(NoSuchAlgorithmException.class));
        storage.put(TestJsonable.newRandom());
    }

    @Test
    public void testGetNoAlgorithm() throws Exception {
        Hash hash = storage.put(IOUtils.toInputStream("Hello, World!"));

        Security.removeProvider(hashProvider.getName());

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(CoreMatchers.isA(NoSuchAlgorithmException.class));
        storage.get(hash);
    }
}