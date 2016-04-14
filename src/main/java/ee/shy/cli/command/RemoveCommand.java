package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * A command to remove given file from its respective directory in the repository.
 */
public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();

        for (String arg : args) {
            repository.remove(Paths.get(arg));
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithArgs("<filename>", "Remove given filename from current commit")
            .addDescription("This command removes <filename> from current commit if present.")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Remove file from the current commit";
    }
}
