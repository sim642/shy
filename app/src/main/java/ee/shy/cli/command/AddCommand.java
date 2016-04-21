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

        for (String arg : args) {
            repository.add(Paths.get(arg));
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithArgs("<file>", "Add given file to current commit")
            .addWithArgs("<directory>", "Add given directory with its contents to current commit")
            .addDescription("Adds files and directories to current commit.")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Add files and directories to current commit";
    }
}
