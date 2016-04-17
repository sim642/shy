package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

import java.io.IOException;

public class InitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository.newEmpty();
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithoutArgs("Initialize a new repository")
            .addDescription("Initializes a new empty repository.")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Initialize a new repository";
    }
}
