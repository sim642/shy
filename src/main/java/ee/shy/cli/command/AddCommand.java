package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.File;
import java.io.IOException;

/**
 * A command to add given file to its respective directory in the repository.
 */
public class AddCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        repository.add(new File(args[0]));
    }

    @Override
    public String getHelp() {
        return "shy add help\n" +
                "\tUsage with arguments:\n" +
                "\t<filename>\n" +
                "\t\t - Add given filename to the index\n\n" +
                "Description:\n" +
                "\tThis command updates the index using the <filename> from the working tree.\n" +
                "\tThe 'index' holds a snapshot of the content of the working tree.\n";
    }

    @Override
    public String getHelpBrief() {
        return "Add file contents to the index";
    }
}
