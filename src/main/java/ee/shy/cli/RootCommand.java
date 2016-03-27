package ee.shy.cli;

import ee.shy.UserPresentableException;
import ee.shy.cli.command.*;

import java.io.IOException;
import java.util.Map;

/**
 * A command representing the root of all commands usable from command line.
 */
public class RootCommand extends SuperCommand {
    public static void main(String[] args) throws IOException {
        try {
            new RootCommand().execute(args);
        } catch (UserPresentableException e) {
            System.err.println(e);
            //System.err.println(e.getLocalizedMessage()); // TODO: 25.03.16 use more user friendly output for release
        }
    }

    public RootCommand() {
        add("init", new InitCommand());
        add("test1", new TestCommand("Test1"));
        add("help", new HelpCommand(this));
        add("add", new AddCommand());
        add("remove", new RemoveCommand());
        add("commit", new CommitCommand());
        add("author", new AuthorCommand());
    }

    @Override
    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("shy help\n")
                .append("\tUsage: shy help [COMMAND]\n\n")
                .append("Possible shy commands:\n");
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
        return null;
    }
}
