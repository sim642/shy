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
        HelptextBuilder helptextBuilder = new HelptextBuilder();
        helptextBuilder.addWithoutArgs("Initialize a new repostiory");
        helptextBuilder.addDescription("Initialize a new empty repository or reinitialize an existing one.");
        helptextBuilder.addDescription("'.shy/' directory will be created with its subdirectories.");
        return helptextBuilder.create();
    }

    @Override
    public String getHelpBrief() {
        return "Initialize a empty repository";
    }
}
