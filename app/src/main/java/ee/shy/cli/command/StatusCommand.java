package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;


import java.io.IOException;

public class StatusCommand implements Command{
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();
        System.out.println(repository.getCurrent().toString());
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithoutArgs("Show information of current state.")
            .addDescription("Shows information of current state.")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Display current state";
    }
}
