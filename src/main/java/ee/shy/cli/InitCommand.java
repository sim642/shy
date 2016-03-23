package ee.shy.cli;

import ee.shy.core.Repository;

import java.io.IOException;

public class InitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = new Repository();
        repository.initialize();
    }

    @Override
    public String getHelp() {
        return "Init command's help(shy help init)";
    }
}
