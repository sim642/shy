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
        return "Usage with arguments:\n" +
                "\t<filename>\n" +
                "\t\t - Remove given filename from current commit\n\n" +
                "Description:\n" +
                "\tThis command removes <filename> from current commit if present.\n";
    }

    @Override
    public String getHelpBrief() {
        return "Remove file from the current commit";
    }
}
