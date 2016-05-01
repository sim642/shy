package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.SshRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A command to clone a remote repository.
 */
public class CloneCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        try {
            URI remoteUri = URI.create(args[0]);
            try (Repository localRepository = LocalRepository.newEmpty();
                 Repository remoteRepository = SshRepository.newRemote(remoteUri)) {
                localRepository.cloneBranch(remoteRepository, Repository.DEFAULT_BRANCH);
            }
        }
        catch (URISyntaxException e) {
            System.err.println("Invalid remote URI");
        }
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
