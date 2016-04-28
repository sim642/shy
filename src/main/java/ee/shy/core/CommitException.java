package ee.shy.core;

import ee.shy.UserPresentableException;

/**
 * Exception to be thrown when trying to commit to invalid current checked out state (tag or commit).
 */
public class CommitException extends RuntimeException implements UserPresentableException {
    public CommitException(CurrentState current) {
        super("cannot commit to " + current);
    }

    @Override
    public String getUserMessage() {
        return getLocalizedMessage() + ": check out or create a branch to commit to";
    }
}
