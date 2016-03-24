package ee.shy.cli;

import ee.shy.core.Repository;

import java.io.IOException;

public class InitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository.newEmpty();
    }

    @Override
    public String getHelp() {
        return "Init command's help(shy help init)";
    }
}
