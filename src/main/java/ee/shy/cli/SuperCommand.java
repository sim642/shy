package ee.shy.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class that acts as a supercommand for multiple subcommands.
 * Used to create tree structure in command handling.
 */
public abstract class SuperCommand implements Command {
    /**
     * Map to store subcommands with respective names.
     */
    private final Map<String, Command> subCommands = new HashMap<>();

    @Override
    public final void execute(String[] args) throws IOException {
        if (args.length > 0 && subCommands.keySet().contains(args[0])) {
            subCommands.get(args[0]).execute(argsSlice(args));
        } else if (args.length > 0){
            System.err.format("%s is not a shy command!%n", args[0]);
        } else {
            System.err.format("Not enough arguments.%n");
        }
    }

    @Override
    public String getHelp(String[] args) {
        if (args.length > 0 && subCommands.keySet().contains(args[0])) {
            return subCommands.get(args[0]).getHelp(argsSlice(args));
        } else {
            return getHelp();
        }
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Command subCommand;
        if ((args.length > 1) // complete subcommand even if fully typed (don't nest yet)
                && ((subCommand = subCommands.get(args[0])) != null)) {
            return subCommand.getCompletion(argsSlice(args));
        }
        else {
            return subCommands.keySet().toArray(new String[0]);
        }
    }

    /**
     * Adds a new subcommand with its name to supercommand.
     * @param commandName command's string value to add
     * @param command a Command object to add
     */
    protected void add(String commandName, Command command) {
        subCommands.put(commandName, command);
    }

    /**
     * Gets tail of arguments.
     * @param args arguments array
     * @return tail of arguments array, or empty array if no tail exists
     */
    private static String[] argsSlice(String[] args) {
        if (args.length <= 1) {
            return new String[0];
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }
}
