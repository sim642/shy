package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Validated;
import ee.shy.storage.Hash;

import java.util.Objects;

/**
 * Class representing a branch.
 */
public class Branch implements Jsonable, Validated {
    public static final String DEFAULT_NAME = "master";

    /**
     * Branch's commit hash.
     */
    private final Hash commit;

    /**
     * Constructs a branch with given commit hash.
     * @param commit commit hash for branch
     */
    public Branch(Hash commit) {
        this.commit = commit;
        assertValid();
    }

    public Hash getHash() {
        return this.commit;
    }

    @Override
    public void assertValid() {
        Objects.requireNonNull(commit, "branch has no commit");
    }
}