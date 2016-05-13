package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Validated;
import ee.shy.storage.Hash;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a commit.
 */
public class Commit implements Jsonable, Validated {
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
        this.parents = new ArrayList<>(builder.parents); // defensive copy
        this.author = builder.author;
        this.time = builder.time;
        this.message = builder.message;

        assertValid();
    }

    public Hash getTree() {
        return this.tree;
    }

    public List<Hash> getParents() {
        return Collections.unmodifiableList(this.parents);
    }

    public Author getAuthor() {
        return this.author;
    }

    public OffsetDateTime getTime() {
        return this.time;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void assertValid() {
        Objects.requireNonNull(tree, "commit has no tree");
        Objects.requireNonNull(parents, "commit has no parents list");
        if (parents.isEmpty())
            throw new IllegalStateException("commit has no parents");
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
        private final List<Hash> parents;

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
            this.time = time.truncatedTo(ChronoUnit.SECONDS);
            return this;
        }

        /**
         * Sets time to current time for commit being built.
         * @return builder itself
         */
        public Builder setTimeCurrent() {
            return setTime(OffsetDateTime.now());
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
