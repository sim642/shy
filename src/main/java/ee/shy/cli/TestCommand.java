package ee.shy.cli;

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
        return "Here be TestCommand's help text.";
    }
}
