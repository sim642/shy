package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

public class RemoveCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository.newExisting().getBranches().remove(args[0]);
    }

    @Override
    public String getHelp() {
        return null;
    }
}
