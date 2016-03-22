package shy.cli;

public class RootCommand extends SuperCommand {
    public static void main(String[] args) {
        RootCommand rootCommand = new RootCommand();
        if (args.length == 0) {
            System.out.println("No command line parameters given. Program will exit.");
            System.exit(0);
            // TODO: 21.03.16 List all commands if no command line parameters given.
        } else if (args[0].equals("help")) {
            System.out.println(rootCommand.getHelp(args));
        } else {
            rootCommand.execute(args);
        }
    }

    public RootCommand() {
        add("init", new Init());
        add("test1", new TestCommand("Test1"));
        add("test2", new TestCommand("Test2"));
    }

    @Override
    protected String superCommandHelp() {
        String helpText = "Here be RootCommand's('shy') help text";
        return helpText;
    }
}
