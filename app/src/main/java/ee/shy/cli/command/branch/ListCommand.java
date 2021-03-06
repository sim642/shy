package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;

import java.io.IOException;

/**
 * Command for listing existing branches.
 */
public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        LocalRepository.newExisting().getBranches().keySet().forEach(System.out::println);
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithoutArgs("List all existing branches")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "List branches";
    }
}
