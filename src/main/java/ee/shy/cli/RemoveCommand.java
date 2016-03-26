package ee.shy.cli;

import ee.shy.core.Repository;

import java.io.File;
import java.io.IOException;

/**
 * Created by grom on 26.03.16.
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
