package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;
import ee.shy.storage.Hash;

import java.io.IOException;

/**
 * A command to search given expression from given commit.
 */
public class SearchCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        String expression = "";
        for (int i = 1; i < args.length ; i++) {
            expression += args[i] + " ";
        }
        expression = expression.trim();

        repository.commitSearch(new Hash(args[0]), expression);
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<commit> <expression>", "Search given expression from a commit")
                .addDescription("This command searches for <expression> from <commit>.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Add file to current commit";
    }
}
