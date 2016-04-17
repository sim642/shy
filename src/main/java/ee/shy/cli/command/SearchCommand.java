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
        repository.commitSearch(new Hash(args[0]), args[1]);
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<commit> <regex>", "Search files matching given regular expression for specified commit")
                .addDescription("Searches content of commits.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Search content of commits";
    }
}
