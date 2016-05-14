package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A command to pull new commits from a remote repository.
 */
public class PullCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository localRepository = LocalRepository.newExisting();
        Remote remote = localRepository.getRemotes().get(args.length >= 1 ? args[0] : Repository.DEFAULT_REMOTE);
        try (SshRepository remoteRepository = SshRepository.newRemote(remote.getURI())) {

            String arg;
            CurrentState current = localRepository.getCurrent();
            switch (current.getType()) { // TODO: 14.05.16 extract this switch
                case BRANCH:
                    arg = current.getBranch();
                    break;

                case TAG:
                    arg = current.getTag();
                    break;

                default:
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
                .addWithoutArgs("Pull new commits from default remote repository (" + Repository.DEFAULT_REMOTE + ")")
                .addDescription("Pulls new commits from remote repositories.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Pull updates from a remote repository";
    }
}
