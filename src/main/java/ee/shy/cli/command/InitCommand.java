package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

public class InitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository.newEmpty();
    }

    @Override
    public String getHelp() {
        return "Usage without arguments:\n" +
                "\t\t - Initialize a new repostiory\n\n" +
                "Description:\n" +
                "\tInitialize a new empty repository or reinitialize an existing one.\n" +
                "\t'.shy/' directory will be created with its subdirectories.\n";
    }

    @Override
    public String getHelpBrief() {
        return "Initialize a empty repository";
    }
}
