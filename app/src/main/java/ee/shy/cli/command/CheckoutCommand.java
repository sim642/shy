package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

/**
 * A command to checkout.
 */
public class CheckoutCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        if (args.length >= 1) {
            Repository repository = LocalRepository.newExisting();
            repository.checkout(args[0]);
        } else
            System.err.println("Not enough parameters. See 'shy help checkout'.");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<hash>", "Check out given commit")
                .addWithArgs("<branch>", "Check out given branch")
                .addDescription("Checks out commits and branches.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Check out commits and branches";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();
        return ArrayUtils.addAll(
                    repository.getBranches().keySet().toArray(new String[0]),
                    repository.getTags().keySet().toArray(new String[0])
                );
    }
}