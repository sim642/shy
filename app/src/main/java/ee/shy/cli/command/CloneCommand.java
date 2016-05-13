package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.LocalRepository;
import ee.shy.core.Repository;
import ee.shy.core.SshRepository;
import ee.shy.io.PathUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * A command to clone a remote repository.
 */
public class CloneCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        try {
            if (args.length == 0) {
                System.err.println("No URI to clone");
                return;
            }

            URI remoteUri = URI.create(args[0]);
            String repositoryName = args.length >= 2 ? args[1] : FilenameUtils.getName(remoteUri.getPath());
            Path localPath = PathUtils.getCurrentPath().resolve(repositoryName);
            try (Repository localRepository = LocalRepository.newEmpty(localPath);
                 Repository remoteRepository = SshRepository.newRemote(remoteUri)) {

                Repository.Fetcher fetcher = new Repository.Fetcher(localRepository, remoteRepository);
                System.out.println("Cloning " + remoteUri + " into " + localRepository.getRootPath());

                if (remoteUri.getFragment() == null) {
                    fetcher.fetchBranches();
                    fetcher.fetchTags();
                    localRepository.checkout(Repository.DEFAULT_BRANCH);
                }
                else {
                    String currentArg = remoteUri.getFragment(); // TODO: 11.05.16 check if arg exists
                    fetcher.fetch(currentArg);
                    localRepository.checkout(currentArg);
                }
            }
        }
        catch (URISyntaxException e) {
            System.err.println("Invalid remote URI");
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithArgs("<uri>", "Clone a remote repository into its respective subdirectory")
                .addWithArgs("<uri> <subdirectory>", "Clone a remote repository into given subdirectory")
                .addDescription("Clones remote repositories.")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Clone a remote repository";
    }
}
