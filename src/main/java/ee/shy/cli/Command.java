package ee.shy.cli;

/**
 * An interface for command line commands.
 */
public interface Command {
    /**
     * Executes the command with given arguments
     * @param args arguments for command
     */
    void execute(String args[]);

    /**
     * Gets help text for the command
     * @param args arguments for command
     * @return help text of the command
     */
    String getHelp(String args[]);
}
