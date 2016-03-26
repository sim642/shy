package ee.shy.cli;

import ee.shy.core.Repository;

import java.io.File;
import java.io.IOException;

/**
 * A command to remove given file from its respective directory in the repository.
 */
public class RemoveCommand implements Command {
    @Override
    public  void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        repository.remove(new File(args[0]));
    }

    @Override
    public String getHelp() {
        return "Remove help message";
    }
}
