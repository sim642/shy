package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Validated;
import ee.shy.storage.Hash;

import java.util.Objects;

/**
 * Class representing repository's current checked out state.
 */
public class CurrentState implements Jsonable, Validated {
    /**
     * Enum of checked out state types.
     */
    public enum Type {
        BRANCH,
        TAG,
        COMMIT,
    }

    /**
     * Hash of currently checked out commit.
     */
    private final Hash commit;

    /**
     * Name of currently checked out branch, or <code>null</code> if a branch isn't checked out.
     */
    private final String branch;

    /**
     * Name of currently checked out tag, or <code>null</code> if a tag isn't checked out.
     */
    private final String tag;

    private CurrentState(Hash commit, String branch, String tag) {
        this.commit = commit;
        this.branch = branch;
        this.tag = tag;

        assertValid();
    }

    /**
     * Creates a new current state object for given commit.
     * @param commit hash of commit
     * @return new current state object
     */
    public static CurrentState newCommit(Hash commit) {
        return new CurrentState(commit, null, null);
    }

    /**
     * Creates a new current state object for given commit and branch.
     * @param commit hash of commit
     * @param branch name of branch
     * @return new current state object
     */
    public static CurrentState newBranch(Hash commit, String branch) {
        return new CurrentState(commit, branch, null);
    }

    /**
     * Creates a new current state object for given commit and tag.
     * @param commit hash of commit
     * @param tag name of tag
     * @return new current state object
     */
    public static CurrentState newTag(Hash commit, String tag) {
        return new CurrentState(commit, null, tag);
    }

    /**
     * Returns type of the checked out state.
     * @return {@link Type} of checked out state
     */
    public Type getType() {
        if (branch != null && tag == null)
            return Type.BRANCH;
        else if (branch == null && tag != null)
            return Type.TAG;
        else
            return Type.COMMIT;
    }

    public Hash getCommit() {
        return commit;
    }

    public String getBranch() {
        return branch;
    }

    public String getTag() {
        return tag;
    }

    /**
     * Returns name of current branch or tag.
     * @return name of branch or tag, or <code>null</code> if state is of neither type
     */
    public String getName() {
        switch (getType()) {
            case BRANCH:
                return getBranch();

            case TAG:
                return getTag();

            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (getType()) {
            case BRANCH:
                return "branch " + branch + " (" + commit + ")";

            case TAG:
                return "tag " + tag + " (" + commit + ")";

            case COMMIT:
                return "commit " + commit;

            default:
                throw new IllegalStateException("current is of unknown type");
        }
    }

    @Override
    public void assertValid() {
        Objects.requireNonNull(commit, "current has no commit");
        if (branch != null && tag != null)
            throw new IllegalStateException("current is branch and tag simultaneously");
    }
}
