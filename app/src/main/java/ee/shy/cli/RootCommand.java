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
        } catch (Exception e) {
            if (e instanceof UserPresentableException) { // interfaces can't be caught
                System.err.println(((UserPresentableException) e).getUserMessage());
            }
            else {
                throw e;
            }
        }
    }

    public RootCommand() {
        add("init", new InitCommand());
        add("help", new HelpCommand(this));
        add("completion", new CompletionCommand(this));
        add("add", new AddCommand());
        add("remove", new RemoveCommand());
        add("commit", new CommitCommand());
        add("author", new AuthorCommand(false));
        add("gauthor", new AuthorCommand(true));
        add("diff", new DiffCommand());
        add("branch", new BranchCommand());
        add("checkout", new CheckoutCommand());
        add("log", new LogCommand());
        add("search", new SearchCommand());
        add("tag", new TagCommand());
        add("clone", new CloneCommand());
        add("remote", new RemoteCommand());
        add("pull", new PullCommand());
        add("push", new PushCommand());
        add("status", new StatusCommand());
        add("show", new ShowCommand());
        add("merge", new MergeCommand());

        add("git-import", new GitImportCommand());
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
