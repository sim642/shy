package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.diff.DiffUtils;
import ee.shy.core.diff.DifferClosure;
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
        Repository repository = LocalRepository.newExisting();
        DifferClosure<?> differClosure = null;

        if (args.length == 0) {
            differClosure = repository.commitDiff();
        }
        else if (args.length == 2) {
            try {
                differClosure = repository.diff(args[0], args[1]);
            } catch (IllegalArgumentException e) {
                // If no hashes given, try to open as file.
                differClosure = new DifferClosure<>(
                        new InputStreamDiffer(),
                        Files.newInputStream(Paths.get(args[0])),
                        Files.newInputStream(Paths.get(args[1]))
                );
            }
        }

        if (differClosure != null) {
            DiffUtils.outputDiff(differClosure.diff());
        }
        else {
            System.err.println("Incorrect amount of parameters for diff command. See shy help diff.");
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
