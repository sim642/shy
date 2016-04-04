package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;

/**
 * A simple command for testing.
 */
public class TestCommand implements Command {
    private final String name;

    public TestCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(String[] args) {
        System.out.println(name);
        Repository repository = Repository.newExisting();
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
