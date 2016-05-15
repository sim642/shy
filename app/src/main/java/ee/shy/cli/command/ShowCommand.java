package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Commit;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.diff.DiffUtils;
import ee.shy.storage.Hash;
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

        Hash commitHash;

        if (args.length > 0) {
            commitHash = repository.parseState(args[0]).getCommit();
        } else {
            commitHash = repository.getCurrent().getCommit();
        }

        LogCommand.printLog(repository.getCommit(commitHash));

        List<String> diff = repository.showDiff(commitHash);

        if (diff != null) {
            System.out.println("Difference(s) with parent: \n");
            DiffUtils.outputDiff(repository.showDiff(commitHash));
        } else {
            System.out.println("No parents.");
        }

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
