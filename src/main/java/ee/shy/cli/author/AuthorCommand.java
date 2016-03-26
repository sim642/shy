package ee.shy.cli.author;

import ee.shy.cli.SuperCommand;

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
        return "'author' help command.";
    }
}
