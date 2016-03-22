package shy.cli;

public class RootCommand extends SuperCommand {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No command line parameters given. Program will exit.");
            System.exit(0);
            // TODO: 21.03.16 List all commands if no command line parameters given.
        }
        new RootCommand().execute(args);
    }

    public RootCommand() {
        add("help", new HelpCommand());
        add("init", new Init());
        add("test1", new TestCommand("Test1"));
        add("test2", new TestCommand("Test2"));
        add("test3", new TestCommand("Test3"));
    }
}
