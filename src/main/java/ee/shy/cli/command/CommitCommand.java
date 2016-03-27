package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * A command for commiting the current commit.
 */
public class CommitCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        if (args.length > 0)
            repository.commit(args[0]);
        else
            System.err.println("No commit message specified");
    }

    @Override
    public String getHelp() {
        return "shy commit help\n" +
                "\tUsage with arguments:\n" +
                "\t<message>\n" +
                "\t\t - Commit changes with given message\n\n" +
                "\tUsage without arguments:\n" +
                "\t\t - Commit changes without message\n\n" +
                "Description:\n" +
                "\tStores the current contents of the index in a new commit with an optional commit message given by user.";
    }

    @Override
    public String getHelpBrief() {
        return "Record changes to the repository";
    }
}
