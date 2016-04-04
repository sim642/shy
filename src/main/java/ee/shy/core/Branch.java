package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.Hash;

/**
 * Class representing a branch.
 */
public class Branch extends Jsonable {
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
    }
}