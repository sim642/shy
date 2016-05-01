package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.io.PathUtils;

import java.io.IOException;

public class InitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = LocalRepository.newEmpty(PathUtils.getCurrentPath());
        System.out.println("Initialized a shy repository in " + repository.getRootPath());
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
