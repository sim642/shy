package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command for removing branches.
 */
public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        if (args.length >= 1) {
            Repository repository = Repository.newExisting();
            if (!args[0].equals(repository.getCurrent().getBranch()))
                repository.getBranches().remove(args[0]);
            else
                System.err.println("Branch '" + args[0] + "' can't be removed because it's checked out");
        } else
            System.err.println("Not enough parameters. See 'shy help branch'.");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<branch name>", "Remove branch named <branch name>.")
                .addDescription("Command to remove branches.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Remove an existing branch";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        return repository.getBranches().keySet().toArray(new String[0]);
    }
}
