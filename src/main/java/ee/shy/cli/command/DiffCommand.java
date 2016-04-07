package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.diff.FileComparator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Command used to see differences between two given objects.
 */
public class DiffCommand implements Command {

    @Override
    public void execute(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Not enough parameters for diff command. See shy help diff.");
        } else {
            FileComparator fileComparator = new FileComparator(Paths.get(args[0]), Paths.get(args[1]));
            fileComparator.outputColorizedDiff();
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
