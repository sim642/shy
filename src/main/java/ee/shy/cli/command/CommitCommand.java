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
        return "Usage with arguments:\n" +
                "\t<message>\n" +
                "\t\t - Commit changes with given message\n\n" +
                "Description:\n" +
                "\tCreate and store a snapshot of current commit with <message>.\n";
    }

    @Override
    public String getHelpBrief() {
        return "Record changes to the repository";
    }
}
