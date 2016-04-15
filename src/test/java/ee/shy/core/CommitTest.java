package ee.shy.core;

import ee.shy.io.IllegalJsonException;
import ee.shy.io.Json;
import ee.shy.storage.Hash;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.Test;

import java.io.IOException;

public class CommitTest {
    @Test(expected = IllegalStateException.class)
    public void testConstructorIllegalTree() {
        new Commit.Builder().addParent(Hash.ZERO).create();
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorIllegalParents() {
        new Commit.Builder().setTree(Hash.ZERO).create();
    }

    @Test
    public void testReadWrite() throws IOException {
        Commit commit = Json.read(getClass().getResourceAsStream("/commit1.json"), Commit.class);
        commit.write(new NullOutputStream());
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalTree() throws Exception {
        Json.read(getClass().getResourceAsStream("/commit2a.json"), Commit.class);
    }

    @Test(expected = IllegalJsonException.class)
    public void testReadIllegalParents() throws Exception {
        Json.read(getClass().getResourceAsStream("/commit2b.json"), Commit.class);
    }
}
