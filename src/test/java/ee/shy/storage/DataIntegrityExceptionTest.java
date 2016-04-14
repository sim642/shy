package ee.shy.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class DataIntegrityExceptionTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGetters() throws Exception {
        Hash expectedHash = Hash.ZERO;
        Hash actualHash = new Hash("2fd4e1c67a2d28fced849ee1bb76e7391b93eb12");

        DataIntegrityException e = new DataIntegrityException(expectedHash, actualHash);

        assertEquals(expectedHash, e.getExpected());
        assertEquals(actualHash, e.getActual());
    }

    @Test
    public void testSameHash() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("integrity");

        new DataIntegrityException(Hash.ZERO, Hash.ZERO);
    }
}