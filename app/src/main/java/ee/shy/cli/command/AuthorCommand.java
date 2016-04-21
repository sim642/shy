package ee.shy.cli.command;

import ee.shy.cli.SuperCommand;
import ee.shy.cli.command.author.EmailCommand;
import ee.shy.cli.command.author.NameCommand;

/**
 * SuperCommand containing commands to alter 'author' file's content.
 */
public class AuthorCommand extends SuperCommand {

    public AuthorCommand() {
        add("name", new NameCommand());
        add("email", new EmailCommand());
    }

    @Override
    public String getHelp() {
        return "Gets and sets author information.";
    }

    @Override
    public String getHelpBrief() {
        return "Get and set author information";
    }
}
