package ee.shy.core;

import ee.shy.io.IllegalJsonException;
import ee.shy.io.Json;
import ee.shy.storage.Hash;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BranchTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalHash() throws Exception {
        new Branch(null);
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalHash() throws Exception {
        Json.read(getClass().getResourceAsStream("branch-illegal-hash.json"), Branch.class);
    }

    @Test
    public void testRead() throws Exception {
        Branch branch = Json.read(getClass().getResourceAsStream("branch.json"), Branch.class);
        assertEquals(Hash.ZERO, branch.getHash());
    }
}