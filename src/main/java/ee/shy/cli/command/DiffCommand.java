package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.diff.InputStreamDiffer;
import ee.shy.core.diff.Util;
import ee.shy.storage.Hash;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.nio.file.Files;
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
            try {
                Hash original = new Hash(args[0]);
                Hash target = new Hash(args[1]);
                throw new NotImplementedException("Commit diff not implemented.");
            } catch (IllegalArgumentException e) {
                // If no hashes given, try to open as file.
                Util.outputDiff(new InputStreamDiffer().diff(
                        Files.newInputStream(Paths.get(args[0])),
                        Files.newInputStream(Paths.get(args[1]))
                ));
            }
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<filename1> <filename2>", "Show user-readable colorized diff output.")
                .addDescription("Shows the differences between two given filenames.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Show differences";
    }
}
