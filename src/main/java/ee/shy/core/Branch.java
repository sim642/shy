package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Required;
import ee.shy.storage.Hash;

/**
 * Class representing a branch.
 */
public class Branch implements Jsonable {
    /**
     * Branch's commit hash.
     */
    @Required
    private final Hash commit;

    /**
     * Constructs a branch with given commit hash.
     * @param commit commit hash for branch
     */
    public Branch(Hash commit) {
        if (commit == null)
            throw new IllegalArgumentException("Branch has no commit");

        this.commit = commit;
    }

    public Hash getHash() {
        return this.commit;
    }
}