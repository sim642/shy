package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;
import ee.shy.core.Tree;
import ee.shy.storage.Hash;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * A command to .
 */
public class CheckoutCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        repository.checkout(new Hash(args[0]));
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<Hash>", "Checkouts commit with given hash.")
                .addDescription("This command checkouts to commit with hash of <hash>.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Checkouts to given commit.";
    }
}