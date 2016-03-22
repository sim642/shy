package shy.cli;

public class HelpCommand extends SuperCommand {

    public HelpCommand() {

        add("commit", (args) -> System.out.println("Help command, commit, lambda test"));

        add("init", (args) -> System.out.println("Help command, init"));
    }
}
