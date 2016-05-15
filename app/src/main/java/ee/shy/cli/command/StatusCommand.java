package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.diff.DiffUtils;

import java.io.IOException;
import java.util.List;

/**
 * Shows information of current state (branch and commit hash)
 */
public class StatusCommand implements Command{
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();
        System.out.println(repository.getCurrent());

        List<String> diffLines = repository.commitDiff().shortDiff();
        if (!diffLines.isEmpty()) {
            System.out.println();
            DiffUtils.outputDiff(diffLines);
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithoutArgs("Show information of current state.")
            .addDescription("Shows information of current state.")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Display current state";
    }
}
