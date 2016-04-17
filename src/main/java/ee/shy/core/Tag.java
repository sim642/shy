package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.Hash;

/**
 * Class representing a tag.
 */
public class Tag implements Jsonable {
    /**
     * Tags's commit hash.
     */
    private final Hash commit;

    /**
     * Tag's message.
     */
    private final String message;

    /**
     * Constructs a tag with given commit hash.
     * @param commit commit hash for tag
     * @param message message for tag
     */
    public Tag(Hash commit, String message) {
        this.commit = commit;
        this.message = message;
    }

    public Hash getHash() {
        return this.commit;
    }
}
