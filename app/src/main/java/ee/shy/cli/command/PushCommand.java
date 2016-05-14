package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * A command to push new commits to a remote repository.
 */
public class PushCommand implements Command {
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
                    System.out.println("Only branches and tags can be pushed");
                    return;
            }

            Repository.Fetcher fetcher = new Repository.Fetcher(remoteRepository, localRepository);
            System.out.println("Pushing " + arg + " to " + remote.getURI());
            fetcher.fetch(arg);
            remoteRepository.checkout(arg);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<remote>", "Push new commits to given remote repository")
                .addWithoutArgs("Push new commits to default remote repository (" + Repository.DEFAULT_REMOTE + ")")
                .addDescription("Pushes new commits to remote repositories.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Push updates to a remote repository";
    }
}
