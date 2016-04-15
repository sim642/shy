package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.Required;
import ee.shy.storage.Hash;

public class Current implements Jsonable {
    public enum State {
        BRANCH(true),
        TAG(false),
        COMMIT(false),
        ;

        private final boolean committable;

        State(boolean committable) {
            this.committable = committable;
        }

        public boolean isCommittable() {
            return committable;
        }
    }

    @Required
    private final Hash commit;

    private final String branch;
    private final String tag;

    public Current(Hash commit, String branch, String tag) {
        this.commit = commit;
        this.branch = branch;
        this.tag = tag;
        // TODO: 15.04.16 check state
    }

    public State getState() {
        if (commit == null)
            throw new IllegalStateException("current must have a commit");
        else if (branch != null && tag != null)
            throw new IllegalStateException("current can't be a branch and a tag simultaneously");
        else if (branch != null && tag == null)
            return State.BRANCH;
        else if (branch == null && tag != null)
            return State.TAG;
        else
            return State.COMMIT;
    }

    public boolean isCommittable() {
        return getState().isCommittable();
    }
}
