package ee.shy.cli.command;

import ee.shy.cli.SuperCommand;
import ee.shy.cli.command.remote.AddCommand;
import ee.shy.cli.command.remote.ListCommand;
import ee.shy.cli.command.remote.RemoveCommand;


public class RemoteCommand extends SuperCommand {
    public RemoteCommand() {
        add("add", new AddCommand());
        add("list", new ListCommand());
        add("remove", new RemoveCommand());
    }

    @Override
    public String getHelp() {
        return "Commands for remote URI creation and modification";
    }

    @Override
    public String getHelpBrief() {
        return "Remote commands";
    }
}

