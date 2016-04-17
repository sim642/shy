package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Author;
import ee.shy.core.Commit;
import ee.shy.core.Repository;
import ee.shy.storage.Hash;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.util.List;

/**
 * A command to display commit history log.
 */
public class LogCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        List<ImmutablePair<Hash, Commit>> commitList;

        if (args.length == 0)
            commitList = repository.log();
        else
            commitList = repository.log(args[0]);
        for (ImmutablePair<Hash, Commit> hashCommitImmutablePair : commitList) {
            String msg = hashCommitImmutablePair.getRight().getMessage();
            if (args.length < 2 || msg.contains(args[1])) {
                printLog(hashCommitImmutablePair.getRight(), msg, hashCommitImmutablePair.getLeft());
            }
        }
    }

    private void printLog(Commit commit, String msg, Hash hash) {
        System.out.println("Commit: " + hash);

        Author author = commit.getAuthor();
        System.out.println("Author: " + author.getName() + "<" + author.getEmail() + ">");

        System.out.println("Time: " + commit.getTime());

        System.out.println("\n \t" + "Message: " + msg + "\n");
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<branch>", "Display commit history of given branch")
                .addWithArgs("<hash>", "Display commit history of given commit")
                .addWithArgs("<branch> <filter>", "Display commit history of given branch, filtered by message")
                .addWithArgs("<hash> <filter>", "Display commit history of given commit, filtered by message")
                .addWithoutArgs("Display commit history of current branch")
                .addDescription("Displays and filters commit history of commits and branches.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Display commit history";
    }
}