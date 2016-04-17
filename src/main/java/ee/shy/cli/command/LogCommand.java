package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Author;
import ee.shy.core.Commit;
import ee.shy.core.Repository;
import ee.shy.storage.Hash;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A command to display commit history log.
 * @throws IOException if flag is not found
 */
public class LogCommand implements Command{
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
            if(args.length < 2) {
                printLog(hashCommitImmutablePair.getRight(), msg, hashCommitImmutablePair.getLeft());
            }
            else if(msg.contains(args[2])) {
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
                .addWithArgs("<hash>", "(optional) Displays commit history log starting from given commit")
                .addWithoutArgs("Displays commit history log of current branch starting from current commit")
                .addDescription("Displays commit history log of current branch")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Display commit history log";
    }
}