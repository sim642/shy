package ee.shy.cli.command;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Author;
import ee.shy.core.Repository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Command for importing existing git repository into new shy repository.
 */
public class GitImportCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newEmpty();
        repository.setAuthor(new Author("git-importer", null));

        try {
            LineIterator lineIterator = IOUtils.lineIterator(
                    new ProcessBuilder("git", "rev-list", "--first-parent", "--reverse", "--pretty=oneline", "HEAD")
                            .start().getInputStream(), StandardCharsets.UTF_8);
            while (lineIterator.hasNext()) {
                String[] pieces = lineIterator.next().split(" ", 2);
                String sha1 = pieces[0];
                String message = pieces[1];

                new ProcessBuilder("git", "checkout", sha1)
                        .start().waitFor();
                repository.add(repository.getRootPath());
                repository.commit(message);
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
                .addWithoutArgs("Imports git repository in current directory into new shy repository")
                .create();
    }

    @Override
    public String getHelpBrief() {
        return "Import git repository into shy repository";
    }
}
