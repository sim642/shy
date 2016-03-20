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
}
