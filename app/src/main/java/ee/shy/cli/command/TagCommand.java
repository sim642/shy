package ee.shy.cli.command;

import ee.shy.cli.SuperCommand;
import ee.shy.cli.command.tag.AddCommand;
import ee.shy.cli.command.tag.ListCommand;
import ee.shy.cli.command.tag.RemoveCommand;

/**
 * Supercommand containing tag related commands.
 */
public class TagCommand extends SuperCommand {
    public TagCommand() {
        add("add", new AddCommand());
        add("list", new ListCommand());
        add("remove", new RemoveCommand());
    }

    @Override
    public String getHelp() {
        return "Commands for tag creation and modification";
    }

    @Override
    public String getHelpBrief() {
        return "Tag commands";
    }
}
