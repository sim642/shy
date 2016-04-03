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
        HelptextBuilder helptextBuilder = new HelptextBuilder();
        helptextBuilder.addWithArgs("test1", "test1Description");
        helptextBuilder.addWithArgs("test2", "test2Description");
        helptextBuilder.addWithArgs("test3", "test3Description");

        helptextBuilder.addWithoutArgs("test4WithoutArgs");
        helptextBuilder.addWithoutArgs("test5WithoutArgs");
        helptextBuilder.addWithoutArgs("test6WithoutArgs");

        helptextBuilder.addDescription("test7Description");
        helptextBuilder.addDescription("test8Description");
        helptextBuilder.addDescription("test9Description");

        return helptextBuilder.create();
    }

    @Override
    public String getHelpBrief() {
        return "Command for testing";
    }
}
