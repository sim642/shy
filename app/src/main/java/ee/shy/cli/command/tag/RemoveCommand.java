package ee.shy.cli.command.tag;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command to remove an existing tag.
 */
public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        if (args.length >= 1) {
            Repository repository = Repository.newExisting();
            if (!args[0].equals(repository.getCurrent().getTag()))
                repository.getTags().remove(args[0]);
            else
                System.err.println("Tag '" + args[0] + "' can't be removed because it's checked out");
        } else
            System.err.println("Not enough parameters. See 'shy help tag'.");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<tag name>", "Remove tag named <tag name>.")
                .addDescription("Command to remove tags.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Remove an existing tag";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        return repository.getTags().keySet().toArray(new String[0]);
    }
}
