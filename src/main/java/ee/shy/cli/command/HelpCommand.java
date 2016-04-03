package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;

import java.io.IOException;

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
        HelptextBuilder helptextBuilder = new HelptextBuilder();
        helptextBuilder.addWithArgs("<command>", "Show <command> help.");
        helptextBuilder.addDescription("Shows help about commands.");
        return helptextBuilder.create();
    }

    @Override
    public String getHelpBrief() {
        return "Show help command's help";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        return rootCommand.getCompletion(args);
    }

}
