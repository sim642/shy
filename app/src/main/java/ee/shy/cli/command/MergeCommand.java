package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;

import java.io.IOException;

public class MergeCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = LocalRepository.newExisting();
        repository.merge(args[0]);
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
