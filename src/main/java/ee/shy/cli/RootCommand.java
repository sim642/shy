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
        add("add", new AddCommand());
        add("remove", new RemoveCommand());
        add("commit", new CommitCommand());
        add("author", new AuthorCommand());
    }

    @Override
    public String getHelp() {
        return "shy help\n" +
                "\tUsage: shy help [COMMAND]\n\n" +
                "Possible shy commands:\n" +
                "\tadd      \t Add file contents to the index\n" +
                "\tauthor   \t Alter or view repository's author\n" +
                "\tcommit   \t Record changes to the repository\n" +
                "\thelp     \t Show help command's help\n" +
                "\tinit     \t Initialize a empty repository\n" +
                "\tremove   \t Remove files from the working tree and index\n";
    }
}
