package shy.cli;

public class RootCommand extends SuperCommand {

    public static void main(String[] args) {
        new RootCommand().execute(args);
    }

    public RootCommand() {
        add("test1", new TestCommand("Test1"));
        add("test2", new TestCommand("Test2"));
        add("test3", new TestCommand("Test3"));
    }
}
