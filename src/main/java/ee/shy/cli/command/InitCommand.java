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
        return "shy init help\n" +
                "\tUsage: shy init\n\n" +
                "Description:\n" +
                "\tInitialize a new empty repository or reinitialize an existing one.\n" +
                "\t'.shy/' directory will be created with its subdirectories.\n" +
                "\tRunning 'shy init' on an existing repository is safe:\n" +
                "\t\tIt will NOT overwrite existing files, but create those what are missing.\n";
    }
}
