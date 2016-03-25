package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.Hash;
import java.time.OffsetDateTime;

/**
 * Class representing a commit.
 */
public class Commit extends Jsonable {
    /**
     * Commit's tree hash.
     */
    private Hash tree;

    /**
     * Commit's parent hashes.
     */
    private Hash[] parents;

    /**
     * Commit's author.
     */
    private Author author;

    /**
     * Commit time.
     */
    private OffsetDateTime time;

    /**
     * Commit message.
     */
    private String message;
}
