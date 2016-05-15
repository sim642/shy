package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Remote;
import ee.shy.core.Repository;
import ee.shy.core.SshRepository;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A command to pull new commits from a remote repository.
 */
public class PullCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository localRepository = LocalRepository.newExisting();
        Remote remote = localRepository.getRemotes().get(args.length >= 1 ? args[0] : Remote.DEFAULT_NAME);
        try (SshRepository remoteRepository = SshRepository.newRemote(remote.getURI())) {

            String arg = localRepository.getCurrent().getName();
            if (arg == null) {
                System.out.println("Only branches and tags can be pulled");
                return;
            }

            Repository.Fetcher fetcher = new Repository.Fetcher(localRepository, remoteRepository);
            System.out.println("Pulling " + arg + " from " + remote.getURI());
            fetcher.fetch(arg);
            localRepository.checkout(arg);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<remote>", "Pull new commits from given remote repository")
                .addWithoutArgs("Pull new commits from default remote repository (" + Remote.DEFAULT_NAME + ")")
                .addDescription("Pulls new commits from remote repositories.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Pull updates from a remote repository";
    }
}
