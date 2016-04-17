package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Repository;
import ee.shy.storage.Hash;

import java.io.IOException;

/**
 * A command to display commit history log.
 * @throws IOException if flag is not found
 */
public class LogCommand implements Command{
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        if (args.length == 0)
            repository.log();
        else if(args.length == 1)
            repository.log(args[0]);
        else if(args.length == 2) {
            if(args[0].equals("--filter"))
                repository.filteredLog(args[1]);
            else
                throw new IOException("Bad flag");
        }
        else {
            if (args[1].equals("--filter"))
                repository.filteredLog(args[0], args[2]);
            else
                throw new IOException("Bad flag");
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<hash>", "(optional) Displays commit history log starting from given commit")
                .addWithoutArgs("Displays commit history log of current branch starting from current commit")
                .addDescription("Displays commit history log of current branch")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Display commit history log";
    }
}