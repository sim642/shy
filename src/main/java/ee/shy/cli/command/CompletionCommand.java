package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.SuperCommand;

import java.io.IOException;
import java.util.Map;

/**
 * A command providing completion for other commands by walking the command structure.
 * Only used as a helper command from completion scripts.
 */
public class CompletionCommand implements Command {
    /**
     * Command to use for starting point of completion.
     */
    private final Command rootCommand;

    /**
     * Constructs a new completion command.
     * @param rootCommand command to use for starting point of completion
     */
    public CompletionCommand(Command rootCommand) {
        this.rootCommand = rootCommand;
    }

    @Override
    public void execute(String[] args) throws IOException {
        Command command = rootCommand;
        for (int i = 0; i <= args.length; i++) { // loop for one extra time for supercommands without arguments
            if (command instanceof SuperCommand) {
                Map<String, Command> subCommands = ((SuperCommand) command).getSubCommands();
                Command subCommand;
                if ((i < args.length - 1) // complete subcommand even if fully typed (don't nest yet)
                        && ((subCommand = subCommands.get(args[i])) != null)) {
                    command = subCommand;
                }
                else {
                    System.out.println(String.join(" ", subCommands.keySet()));
                    break;
                }
            }
            else if (command instanceof HelpCommand) {
                command = ((HelpCommand) command).getRootCommand(); // changed command point without parsing extra argument
                i--; // step back for that extra argument
            }
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    /**
     * Get starting point command.
     * @return starting point command
     */
    public Command getRootCommand() {
        return rootCommand;
    }
}
