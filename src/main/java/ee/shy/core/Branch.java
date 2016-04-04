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
    private final Hash hash;

    /**
     * Constructs a branch with given commit hash.
     * @param hash commit hash for branch
     */
    public Branch(Hash hash) {
        this.hash = hash;
    }
}