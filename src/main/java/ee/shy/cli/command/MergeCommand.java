package ee.shy.cli.command;

import difflib.PatchFailedException;
import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;

public class MergeCommand implements Command{

    @Override
    public void execute(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Not enough parameters for merge. See 'shy help merge'");
        }
        Repository repository = Repository.newExisting();
        try {
            repository.merge(args[0]);
        } catch (PatchFailedException e) {
            System.err.println("Merge conflict occurred!");
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<branch1> <branch2>", "Merge two given branches")
                .addDescription("Three-way merge given parameters. Create a commit on current branch"
                ).create();
    }

    @Override
    public String getHelpBrief() {
        return "Merge functionality";
    }
}
