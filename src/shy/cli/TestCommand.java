package shy.cli;

/**
 * Created by tiit on 15.03.16.
 */
public class TestCommand implements Command {

    private final String name;

    public TestCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(String[] args) {
        System.out.println(name);

    }
}
