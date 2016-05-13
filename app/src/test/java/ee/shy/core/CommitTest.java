package ee.shy.core;

import ee.shy.io.IllegalJsonException;
import ee.shy.io.Json;
import ee.shy.storage.Hash;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.Test;

import java.io.IOException;

public class CommitTest {
    @Test(expected = NullPointerException.class)
    public void testConstructorIllegalTree() {
        new Commit.Builder().addParent(Hash.ZERO).create();
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorIllegalParents() {
        new Commit.Builder().setTree(Hash.ZERO).create();
    }

    @Test
    public void testReadWrite() throws IOException {
        Commit commit = Json.read(getClass().getResourceAsStream("commit.json"), Commit.class);
        commit.write(new NullOutputStream());
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalTree() throws Exception {
        Json.read(getClass().getResourceAsStream("commit-illegal-tree.json"), Commit.class);
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalParents() throws Exception {
        Json.read(getClass().getResourceAsStream("commit-illegal-parents.json"), Commit.class);
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalParentsEmpty() throws Exception {
        Json.read(getClass().getResourceAsStream("commit-illegal-empty-parents.json"), Commit.class);
    }
}
