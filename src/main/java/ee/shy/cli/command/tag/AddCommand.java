package ee.shy.cli.command.tag;

import ee.shy.cli.Command;
import ee.shy.core.Repository;
import ee.shy.core.Tag;

import java.io.IOException;

public class AddCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        if (args.length >= 2 ) {
            if (!repository.getTags().containsKey(args[0])) {
                repository.getTags().put(args[0], new Tag(repository.getCurrent().getCommit(), args[1]));
            } else {
                System.err.println("Tag " + args[0] + " already exists");
            }
        } else {
            System.err.println("Not enough parameters. See 'shy tag help'.");
        }
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
