package ee.shy.cli.command.author;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Author;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command to get or set the repository's author's email.
 */
public class EmailCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        Author author = repository.getAuthor();

        if (args.length == 0) {
            if (author.getEmail() != null) {
                System.out.println(author.getEmail());
            } else {
                System.out.println("Repository's author's email is not set. See 'shy help author'.");
            }
        } else {
            Author authorNew = new Author(author.getName(), args[0]);
            repository.setAuthor(authorNew);
        }
    }

    @Override
    public String getHelp() {
        return new HelptextBuilder()
            .addWithArgs("<email>", "Set author's email")
            .addWithoutArgs("Get author's email")
            .create();
    }

    @Override
    public String getHelpBrief() {
        return "Get and set author's email";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        Author author = repository.getAuthor();
        return new String[]{author.getEmail()};
    }
}
