package ee.shy.core;

import ee.shy.UserPresentableException;

public class RepositoryNotFoundException extends UserPresentableException {
    public RepositoryNotFoundException() {
        super("repository", "not found");
    }
}
