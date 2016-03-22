package shy.cli;

public class HelpCommand implements Command {

    private final Command rootCommand;

    public HelpCommand(Command rootCommand) {
        this.rootCommand = rootCommand;
    }

    @Override
    public void execute(String[] args) {
        System.out.println(rootCommand.getHelp(args));
    }

    @Override
    public String getHelp(String[] args) {
        return "Help's help";
    }
}
