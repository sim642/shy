package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * A command for commiting the current commit.
 */
public class CommitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        if (args.length > 0)
            repository.commit(args[0]);
        else
            System.err.println("No commit message specified");
    }

    @Override
    public String getHelp() {
        return "commit current commit";
    }
}
