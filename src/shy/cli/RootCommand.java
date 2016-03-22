package shy.cli;

public class RootCommand extends SuperCommand {
    public static void main(String[] args) {
        new RootCommand().execute(args);
    }

    public RootCommand() {
        add("init", new Init());
        add("test1", new TestCommand("Test1"));
        add("test2", new TestCommand("Test2"));
        add("help", new HelpCommand(this));
    }

    @Override
    protected String superCommandHelp() {
        String helpText = "Here be RootCommand's('shy') help text";
        return helpText;
    }
}
