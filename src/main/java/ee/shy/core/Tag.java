package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Required;
import ee.shy.storage.Hash;

/**
 * Class representing a tag.
 */
public class Tag implements Jsonable {
    /**
     * Tags's commit hash.
     */
    @Required
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
        if (commit == null)
            throw new IllegalArgumentException("Tag has no commit");

        this.commit = commit;
        this.message = message;
    }

    public Hash getHash() {
        return this.commit;
    }

    public String getMessage() {
        return message;
    }
}
