package ee.shy.cli.command;

import ee.shy.cli.Command;

import java.io.IOException;

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
        System.out.println(String.join(" ", rootCommand.getCompletion(args)));
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
