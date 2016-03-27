package ee.shy.cli.command;

import ee.shy.cli.Command;

/**
 * A command providing help about other commands by retrieving their help text.
 */
public class HelpCommand implements Command {
    /**
     * Command to use for starting point of help texts.
     */
    private final Command rootCommand;

    /**
     * Constructs a new help command.
     * @param rootCommand command to use for starting point of help texts
     */
    public HelpCommand(Command rootCommand) {
        this.rootCommand = rootCommand;
    }

    @Override
    public void execute(String[] args) {
        System.out.println(rootCommand.getHelp(args));
    }

    @Override
    public String getHelp() {
        return "Usage with arguments:\n" +
                "\t<command>\n" +
                "\t\t - Show <command> help.\n\n" +
                "Description:\n" +
                "\tShows help about commands.\n";
    }

    @Override
    public String getHelpBrief() {
        return "Show help command's help";
    }
}
