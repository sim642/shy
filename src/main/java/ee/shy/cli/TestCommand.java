package ee.shy.cli;

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
        for (String arg : args) {
            System.out.println(arg);
        }
    }

    @Override
    public String getHelp() {
        return "Here be TestCommand's help text.";
    }
}
