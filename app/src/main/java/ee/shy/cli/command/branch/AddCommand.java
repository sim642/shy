package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Branch;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command for adding new branches.
 */
public class AddCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        if (args.length >= 1) {
            Repository repository = LocalRepository.newExisting();
            if (!repository.getBranches().containsKey(args[0]))
                repository.getBranches().put(args[0], new Branch(repository.getCurrent().getCommit()));
            else
                System.err.println("Branch " + args[0] + " already exists");
        } else
            System.err.println("Not enough parameters. See 'shy help branch'.");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<branch>", "Create a new branch with given name from latest commit")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Create branches";
    }
}
