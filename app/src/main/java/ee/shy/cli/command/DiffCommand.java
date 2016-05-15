package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.diff.DiffUtils;
import ee.shy.core.diff.InputStreamDiffer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Command used to see differences between two given objects.
 */
public class DiffCommand implements Command {

    @Override
    public void execute(String[] args) throws IOException {
        if (args.length == 0) {
            Repository repository = LocalRepository.newExisting();
            DiffUtils.outputDiff(repository.commitDiff());
        }
        else if (args.length >= 2) {
            try {
                Repository repository = LocalRepository.newExisting();
                DiffUtils.outputDiff(repository.diff(args[0], args[1]));
            } catch (IllegalArgumentException e) {
                // If no hashes given, try to open as file.
                DiffUtils.outputDiff(new InputStreamDiffer().diff(
                        Files.newInputStream(Paths.get(args[0])),
                        Files.newInputStream(Paths.get(args[1]))
                ));
            }
        }
        else {
            System.err.println("Not enough parameters for diff command. See shy help diff.");
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<commit1> <commit1>", "Show user-readable diff between two commits")
                .addWithArgs("<file1> <file2>", "Show user-readable diff between two files")
                .addWithoutArgs("Show user-readable diff between latest commit and current commit")
                .addDescription("Shows differences between different items.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Show differences";
    }
}
