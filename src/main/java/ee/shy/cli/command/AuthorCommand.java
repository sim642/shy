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
        return "shy author help\n" +
                "\tUsage: shy author <email | name> [new_value]\n\n" +
                "Description:\n" +
                "\t If command is executed without [new_value], current value is shown.\n" +
                "\t If command is executed with [new_value], set current repository's author's details to [new_value]\n" +
                "\t See 'shy help author email' or 'shy help author name'\n";
    }
}
