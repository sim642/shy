package ee.shy.cli.command.author;

import ee.shy.cli.Command;
import ee.shy.cli.HelptextBuilder;
import ee.shy.core.Author;
import ee.shy.core.Repository;

import java.io.IOException;

/**
 * Command to set or get the repository's author's name.
 */
public class NameCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        Author author = repository.getAuthor();

        if (args.length == 0) {
            if (author.getName() != null) {
                System.out.println(author.getName());
            } else {
                System.out.println("Repository's author's name is not set. See 'shy help author'.");
            }
        } else {
            Author authorNew = new Author(args[0], author.getEmail());
            repository.setAuthor(authorNew);
        }
    }

    @Override
    public String getHelp() {
        HelptextBuilder helptextBuilder = new HelptextBuilder();
        helptextBuilder.addWithArgs("<name>", "set author's name.");
        helptextBuilder.addWithoutArgs("get author's name.");
        return helptextBuilder.create();
    }

    @Override
    public String getHelpBrief() {
        return "View current or alter 'author' file's name field.";
    }

    @Override
    public String[] getCompletion(String[] args) throws IOException {
        Repository repository = Repository.newExisting();
        Author author = repository.getAuthor();
        return new String[]{author.getName()};
    }
}
