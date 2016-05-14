package ee.shy.cli.command.remote;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command for removing remotes.
 */
public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        if (args.length >= 1) {
            Repository repository = LocalRepository.newExisting();
            repository.getRemotes().remove(args[0]);
        } else
            System.err.println("Not enough parameters. See 'shy help remote'.");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<remote>", "Remove the remote URI")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Remove remote URI";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();
        return repository.getRemotes().keySet().toArray(new String[0]);
    }
}
