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
        String[] completions = rootCommand.getCompletion(args);
        for (String completion : completions) {
            if (completion.contains(" "))
                System.out.println('"' + completion + '"');
            else
                System.out.println(completion);
        }
    }

    @Override
    public String getHelp() {
        return "Usage with arguments:\n" +
                "\t<arg1> <arg2> ...\n" +
                "\t\t - Generate completions list for given arguments list.\n" +
                "\n" +
                "Description:\n" +
                "\tGenerates a list of completions to be used by completion scripts.\n";
    }

    @Override
    public String getHelpBrief() {
        return "Generates completions list";
    }
}
