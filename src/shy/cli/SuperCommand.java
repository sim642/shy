package shy.cli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tiit on 12.03.16.
 */
public abstract class SuperCommand implements Command {

    private Map<String, Command> subCommands = new HashMap<>();

    @Override
    public final void execute(String[] args) {
        subCommands.get(args[0]).execute(argsSlice(args));
    }

    protected void add(String commandName, Command command) {
        subCommands.put(commandName, command);
    }

    private static String[] argsSlice(String[] args) {
        if (args.length <= 1) {
            return new String[0];
        } else {
            return Arrays.copyOfRange(args, 1, args.length - 1);
        }
    }
}
