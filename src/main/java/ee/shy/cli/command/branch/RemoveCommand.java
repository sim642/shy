package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command for removing branches.
 */
public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        if (!args[0].equals(repository.getCurrent().getBranch()))
            repository.getBranches().remove(args[0]);
        else
            System.err.println("Branch " + args[0] + " can't be removed because it's checked out");
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
