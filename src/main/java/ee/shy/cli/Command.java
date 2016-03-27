package ee.shy.cli;

import java.io.IOException;

/**
 * An interface for command line commands.
 */
public interface Command {
    /**
     * Executes the command with given arguments
     * @param args arguments for command
     * @throws IOException if I/O problem occurred during execution of the command
     */
    void execute(String args[]) throws IOException;

    /**
     * Gets help text for the command
     * @param args arguments for command
     * @return help text of the command
     */
    default String getHelp(String args[]) {
        return getHelp();
    }

    /**
     * Gets help text for the command
     * @return help text of the command
     */
    String getHelp();

    /**
     * Gets a brief one-line description of a command
     * @return command description
     */
    String getHelpBrief();
}
