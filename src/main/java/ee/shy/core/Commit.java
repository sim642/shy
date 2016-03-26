package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.Hash;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a commit.
 */
public class Commit extends Jsonable {
    /**
     * Commit's tree hash.
     */
    private final Hash tree;

    /**
     * Commit's parent hashes.
     */
    private final List<Hash> parents;

    /**
     * Commit's author.
     */
    private final Author author;

    /**
     * Commit time.
     */
    private final OffsetDateTime time;

    /**
     * Commit message.
     */
    private final String message;

    /**
     * Constructs a commit from its builder.
     * @param builder commit builder
     */
    public Commit(Builder builder) {
        this.tree = builder.tree;
        this.parents = builder.parents;
        this.author = builder.author;
        this.time = builder.time;
        this.message = builder.message;
    }

    /**
     * Builder class for {@link Commit}.
     */
    public static class Builder implements ee.shy.Builder<Commit> {
        /**
         * @see Commit#tree
         */

        private Hash tree;
        /**
         * @see Commit#parents
         */
        private List<Hash> parents;

        /**
         * @see Commit#author
         */
        private Author author;

        /**
         * @see Commit#time
         */
        private OffsetDateTime time;

        /**
         * @see Commit#message
         */
        private String message;

        /**
         * Constructs a new commit builder.
         */
        public Builder() {
            this.parents = new ArrayList<>();
        }

        /**
         * Sets tree hash for commit being built.
         * @param tree tree hash
         * @return builder itself
         */
        public Builder setTree(Hash tree) {
            this.tree = tree;
            return this;
        }

        /**
         * Adds a parent hash to commit being built.
         * @param parent parent hash
         * @return builder itself
         */
        public Builder addParent(Hash parent) {
            this.parents.add(parent);
            return this;
        }

        /**
         * Sets author for commit being built.
         * @param author author
         * @return builder itself
         */
        public Builder setAuthor(Author author) {
            this.author = author;
            return this;
        }

        /**
         * Sets time for commit being built.
         * @param time time
         * @return builder itself
         */
        public Builder setTime(OffsetDateTime time) {
            this.time = time;
            return this;
        }

        /**
         * Sets time to current time for commit being built.
         * @return builder itself
         */
        public Builder setTimeCurrent() {
            return setTime(OffsetDateTime.now()); // TODO: 26.03.16 remove ms from output
        }

        /**
         * Sets message for commit being built.
         * @param message message
         * @return builder itself
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        @Override
        public Commit create() {
            return new Commit(this);
        }
    }
}
