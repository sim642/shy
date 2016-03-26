package ee.shy.cli;

import ee.shy.core.Repository;

import java.io.IOException;

/**
 * A command for commiting the current commit.
 */
public class CommitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        System.out.println(repository.createCommitTree());
    }

    @Override
    public String getHelp() {
        return "commit current commit";
    }
}
