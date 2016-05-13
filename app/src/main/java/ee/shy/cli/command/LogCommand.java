package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Commit;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.storage.Hashed;

import java.io.IOException;
import java.util.List;

/**
 * A command to display commit history log.
 */
public class LogCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();
        List<Hashed<Commit>> commits = repository.log(args.length >= 1 ? args[0] : null);
        for (Hashed<Commit> hashedCommit : commits) {
            String msg = hashedCommit.getValue().getMessage();
            if (args.length < 2 || msg.contains(args[1])) {
                printLog(hashedCommit);
            }
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