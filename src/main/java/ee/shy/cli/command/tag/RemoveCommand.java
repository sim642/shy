package ee.shy.cli.command.tag;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        if (!args[0].equals(repository.getCurrent().getTag()))
            repository.getTags().remove(args[0]);
        else
            System.err.println("Tag " + args[0] + " can't be removed because it's checked out");
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
    // TODO: 18.04.16 Add completion command.
}
