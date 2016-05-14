package ee.shy.cli.command.remote;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Remote;
import ee.shy.core.Repository;

import java.io.IOException;
import java.net.URI;

/**
 * Command for adding new remotes.
 */
public class AddCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        if (args.length >= 2) {
            Repository repository = LocalRepository.newExisting();
            if (!repository.getRemotes().containsKey(args[0]))
                repository.getRemotes().put(args[0], new Remote(URI.create(args[1])));
            else
                System.err.println("Remote " + args[0] + " already exists");
        } else
            System.err.println("Not enough parameters. See 'shy help remote'.");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<name> <uri>", "Create a new remote with given name and uri")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Create remotes";
    }
}
