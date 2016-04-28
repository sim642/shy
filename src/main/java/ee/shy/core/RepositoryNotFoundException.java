package ee.shy.core;

import ee.shy.UserPresentableException;

/**
 * Exception to be thrown when repository to use was not found when it was required.
 */
public class RepositoryNotFoundException extends RuntimeException implements UserPresentableException {
    public RepositoryNotFoundException() {
        super("repository not found");
    }
}
