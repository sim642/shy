package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.Tree;
import ee.shy.core.diff.DiffUtils;
import ee.shy.core.diff.DifferClosure;
import ee.shy.storage.Hash;

import java.io.IOException;

/**
 * A command to display information of a commit and its difference with its parent.
 */
public class ShowCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();

        Hash commitHash = repository.parseState(args.length > 0 ? args[0] : null).getCommit();

        LogCommand.printLog(repository.getCommit(commitHash));

        DifferClosure<Tree> differClosure = repository.showDiff(commitHash);
        if (differClosure != null)
            DiffUtils.outputDiff(differClosure.diff());
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<arg>", "Display commit information and its difference with its parent")
                .addWithoutArgs("Display commit information and its difference with its parent")
                .addDescription("Displays commit information and difference with its parent.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Display commit information and difference.";
    }
}
