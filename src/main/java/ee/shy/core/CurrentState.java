package ee.shy.core;

import ee.shy.io.CheckState;
import ee.shy.io.Jsonable;
import ee.shy.io.Required;
import ee.shy.storage.Hash;

/**
 * Class representing repository's current checked out state.
 */
public class CurrentState implements Jsonable {
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
    @Required
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
        checkState();
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

    @CheckState
    private void checkState() {
        if (commit == null)
            throw new IllegalStateException("current must have a commit");
        else if (branch != null && tag != null)
            throw new IllegalStateException("current can't be a branch and a tag simultaneously");
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
}
