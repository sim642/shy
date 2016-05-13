package ee.shy.cli.command;

import difflib.PatchFailedException;
import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.merge.InputStreamMerger;
import ee.shy.core.merge.Merger;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A simple command for testing.
 */
public class TestCommand implements Command {

    public void execute(String[] args) throws IOException {
        Merger<InputStream> merger = new InputStreamMerger();
        try {
            InputStream is = merger.merge(Files.newInputStream(Paths.get(args[0])),
                    Files.newInputStream(Paths.get(args[1])),
                    Files.newInputStream(Paths.get(args[2])));
            IOUtils.copy(is, System.out);
        } catch (PatchFailedException e) {
            System.err.println("Merge failed");
            // TODO: 9.05.16 Present error to the user in a polite manner
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithArgs("test1", "test1Description")
            .addWithArgs("test2", "test2Description")
            .addWithArgs("test3", "test3Description")

            .addWithoutArgs("test4WithoutArgs")
            .addWithoutArgs("test5WithoutArgs")
            .addWithoutArgs("test6WithoutArgs")

            .addDescription("test7Description")
            .addDescription("test8Description")
            .addDescription("test9Description")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Command for testing";
    }
}
