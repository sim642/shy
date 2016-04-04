package ee.shy.cli.command;

import ee.shy.cli.SuperCommand;
import ee.shy.cli.command.branch.*;
import ee.shy.cli.command.branch.AddCommand;
import ee.shy.cli.command.branch.RemoveCommand;

public class BranchCommand extends SuperCommand {
    public BranchCommand() {
        add("add", new AddCommand());
        add("list", new ListCommand());
        add("remove", new RemoveCommand());
    }

    @Override
    public String getHelp() {
        return null;
    }
}
