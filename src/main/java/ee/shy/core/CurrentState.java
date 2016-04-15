package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Required;
import ee.shy.storage.Hash;

public class CurrentState implements Jsonable {
    public enum Type {
        BRANCH,
        TAG,
        COMMIT,
    }

    @Required
    private final Hash commit;

    private final String branch;

    private final String tag;

    public CurrentState(Hash commit, String branch, String tag) {
        this.commit = commit;
        this.branch = branch;
        this.tag = tag;
        // TODO: 15.04.16 check state
    }

    public Type getType() {
        if (commit == null)
            throw new IllegalStateException("current must have a commit");
        else if (branch != null && tag != null)
            throw new IllegalStateException("current can't be a branch and a tag simultaneously");
        else if (branch != null && tag == null)
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
}