package ee.shy.cli;

/**
 * A command representing the root of all commands usable from command line.
 */
public class RootCommand extends SuperCommand {
    public static void main(String[] args) {
        new RootCommand().execute(args);
    }

    public RootCommand() {
        add("init", new InitCommand());
        add("test1", new TestCommand("Test1"));
        add("test2", new TestCommand("Test2"));
        add("help", new HelpCommand(this));
    }

    @Override
    public String getHelp() {
        return "Here be RootCommand's('shy') help text";
    }
}
