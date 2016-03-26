package ee.shy.cli;

import ee.shy.core.Repository;

import java.io.File;
import java.io.IOException;

public class AddCommand implements Command{

    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        repository.add(new File(args[0]));
    }

    @Override
    public String getHelp() {
        return null;
    }
}
