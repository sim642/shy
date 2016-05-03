package ee.shy.cli.command;

import difflib.PatchFailedException;
import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.merge.Merge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * A simple command for testing.
 */
public class TestCommand implements Command {

    public void execute(String[] args) throws IOException {
        List<String> strings;
        try {
            strings = Merge.applyPatch(Files.newInputStream(Paths.get(args[2])), Merge.generatePatch(
                    Files.newInputStream(Paths.get(args[0])),
                    Files.newInputStream(Paths.get(args[1]))
            ));
        } catch (PatchFailedException e) {
            throw new RuntimeException(e);
            // TODO: 3.05.16 Make an user-presentable exception for merge failure
        }
        strings.forEach(System.out::println);
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
