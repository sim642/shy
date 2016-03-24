package ee.shy.core;

public class RepositoryNotFoundException extends RuntimeException {
    public RepositoryNotFoundException() {
        super("Repository not found!");
    }
}
