package ee.shy.cli;

import ee.shy.UserPresentableException;
import ee.shy.cli.command.*;

import java.io.IOException;

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
        add("completion", new CompletionCommand(this));
        add("add", new AddCommand());
        add("remove", new RemoveCommand());
        add("commit", new CommitCommand());
        add("author", new AuthorCommand());
    }

    @Override
    public String getHelp() {
        return "shy version control system";
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
