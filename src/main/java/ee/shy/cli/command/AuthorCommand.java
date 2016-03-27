package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.SuperCommand;
import ee.shy.cli.command.author.EmailCommand;
import ee.shy.cli.command.author.NameCommand;

import java.util.Map;

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
        StringBuilder sb = new StringBuilder();
        sb.append("shy author help\n")
                .append("\tUsage: shy author [COMMAND]\n\n")
                .append("Possible shy author commands:\n");
        for (Map.Entry<String, Command> stringCommandEntry : subCommands.entrySet()) {
            sb.append("\t")
                    .append(stringCommandEntry.getKey())
                    .append("\t")
                    .append(stringCommandEntry.getValue().getHelpBrief())
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getHelpBrief() {
        return "Alter or view repository's author";
    }
}
