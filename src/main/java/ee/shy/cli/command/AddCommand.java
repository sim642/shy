package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * A command to add given file to its respective directory in the repository.
 */
public class AddCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        repository.add(Paths.get(args[0]));
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithArgs("<filename>", "Add given filename to current commit")
            .addDescription("This command adds <filename> to current commit.")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Add file to current commit";
    }
}
