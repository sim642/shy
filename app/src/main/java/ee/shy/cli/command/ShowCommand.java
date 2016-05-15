package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Commit;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.diff.DiffUtils;
import ee.shy.storage.Hashed;

import java.io.IOException;
import java.util.List;

/**
 * A command to display information of a commit and its difference with its parent.
 */
public class ShowCommand implements Command {

    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();
        List<Hashed<Commit>> commits = repository.log(args.length >= 1 ? args[0] : null);
        String msg = commits.get(0).getValue().getMessage();
        printLog(commits.get(0));

        if (commits.size() > 1) {
            System.out.println("Difference(s) with parent: \n");

            DiffUtils.outputDiff(repository.diff(commits.get(1).getHash().toString(), commits.get(0).getHash().toString()));
        } else {
            System.out.println("Given commit has no parent to show difference with.");
        }
    }

    private void printLog(Hashed<Commit> hashedCommit) {
        Commit commit = hashedCommit.getValue();

        System.out.println("Commit: " + hashedCommit.getHash());
        System.out.println("Author: " + commit.getAuthor());
        System.out.println("Time: " + commit.getTime());

        System.out.println("\n \t" + commit.getMessage() + "\n");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<hash>", "Display commit information and its difference with its parent (if exists)" +
                        " of a commit with given hash.")
                .addWithoutArgs("Display commit information and its difference with its parent (if exists)" +
                        " of current commit.")
                .addDescription("Displays commit information and difference with its parent (if exists).")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Display commit information and difference.";
    }
}
