package shy.cli;

/**
 * An interface for new commands
 */
public interface Command {

    /**
     * Each command has an execute method which controls program's functionality.
     * @param args parameters for command
     */
    void execute(String args[]);

    /**
     * Each command has a getHelp method which provides the user with information concerning the
     * proper usage and details of the command.
     * If getHelp is called on a {@link SuperCommand} object, it calls it's subCommand's getHelp method
     * unless arguments(args) are empty.
     * @param args arguments.
     * @return a string containing information about Command object.
     */
    String getHelp(String args[]);
}
