package ee.shy.cli.command.branch;

import ee.shy.cli.Command;
import ee.shy.core.Branch;
import ee.shy.core.Repository;
import ee.shy.storage.Hash;

import java.io.IOException;

/**
 * Command for adding new branches.
 */
public class AddCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        // TODO: 4.04.16 check branch existence
        repository.getBranches().put(args[0], new Branch(new Hash(args[1]))); // TODO: 4.04.16 use current hash
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
