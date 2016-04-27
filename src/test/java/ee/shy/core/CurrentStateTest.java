package ee.shy.core;

import ee.shy.io.IllegalJsonException;
import ee.shy.io.Json;
import ee.shy.storage.Hash;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class CurrentStateTest {
    @Test
    public void testType() throws Exception {
        assertEquals(CurrentState.Type.COMMIT, CurrentState.newCommit(Hash.ZERO).getType());
        assertEquals(CurrentState.Type.BRANCH, CurrentState.newBranch(Hash.ZERO, "foo").getType());
        assertEquals(CurrentState.Type.TAG, CurrentState.newTag(Hash.ZERO, "foo").getType());
    }

    @Test
    public void testRead() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("currentstate-commit.json")) {
            assertEquals(CurrentState.Type.COMMIT, Json.read(is, CurrentState.class).getType());
        }

        try (InputStream is = getClass().getResourceAsStream("currentstate-branch.json")) {
            assertEquals(CurrentState.Type.BRANCH, Json.read(is, CurrentState.class).getType());
        }

        try (InputStream is = getClass().getResourceAsStream("currentstate-tag.json")) {
            assertEquals(CurrentState.Type.TAG, Json.read(is, CurrentState.class).getType());
        }
    }

    @Test(expected = IllegalJsonException.class)
    public void testIllegalCommit() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("currentstate-illegal-commit.json")) {
            Json.read(is, CurrentState.class);
        }
    }

    @Test(expected = IllegalJsonException.class)
    public void testIllegalDual() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("currentstate-illegal-dual.json")) {
            Json.read(is, CurrentState.class);
        }
    }
}