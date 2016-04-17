package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * A command to checkout.
 */
public class CheckoutCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        repository.checkout(args[0]);
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<String>", "Checkouts to given commit or branch")
                .addDescription("This command checkouts to commit with hash of <string>.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Checkouts to given commit.";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        return repository.getBranches().keySet().toArray(new String[0]);
    }
}