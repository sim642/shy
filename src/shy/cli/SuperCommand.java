package shy.cli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class that acts as a supercommand of multiple subcommands.
 * Used to create tree structure in command handling.
 */
public abstract class SuperCommand implements Command {

    /**
     * Hashmap to store SuperCommand's subcommands and their corresponding name string values
     */
    private Map<String, Command> subCommands = new HashMap<>();

    @Override
    public final void execute(String[] args) {
        if (subCommands.keySet().contains(args[0])) {
            subCommands.get(args[0]).execute(argsSlice(args));
        } else {
            System.err.format("%s is not a shy command!%n", args[0]);
        }
    }

    /**
     * Adds a new command and its string name value to {@link #subCommands}
     * @param commandName command's string value to add
     * @param command a Command object to add
     */
    protected void add(String commandName, Command command) {
        subCommands.put(commandName, command);
    }

    /**
     * Static method to separate the head from tail.
     * @param args String array object to slice
     * @return If array has more than one element: return its tail. Else return empty string array.
     */
    private static String[] argsSlice(String[] args) {
        if (args.length <= 1) {
            return new String[0];
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }
}
