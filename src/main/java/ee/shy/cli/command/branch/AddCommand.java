package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.core.Branch;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command for adding new branches.
 */
public class AddCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        if (!repository.getBranches().containsKey(args[0]))
            repository.getBranches().put(args[0], new Branch(repository.getCurrent().getCommit()));
        else
            System.err.println("Branch " + args[0] + " already exists");
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
