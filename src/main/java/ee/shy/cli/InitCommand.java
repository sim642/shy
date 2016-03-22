package ee.shy.cli;

import ee.shy.core.Repository;

import java.io.File;

public class InitCommand implements Command {
    @Override
    public void execute(String[] args) {
        String root = System.getProperty("user.dir");
        File repositoryDirectory = new File(root + "/.shy/");
        Repository repository = new Repository(new File(root), repositoryDirectory);
        repository.initialize();
    }

    @Override
    public String getHelp() {
        return "Init command's help(shy help init)";
    }
}
