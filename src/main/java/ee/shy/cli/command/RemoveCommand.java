package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.File;
import java.io.IOException;

/**
 * A command to remove given file from its respective directory in the repository.
 */
public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        repository.remove(new File(args[0]));
    }

    @Override
    public String getHelp() {
        return "shy remove help\n" +
                "\tUsage: shy remove <filename>\n\n" +
                "Description:\n" +
                "\tRemove given <filename> from the index.\n";
    }
}
