package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command for listing existing branches.
 */
public class ListCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository.newExisting().getBranches().keySet().forEach(System.out::println);
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
