package ee.shy.cli.command.author;

import ee.shy.cli.Command;
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
        return "shy author name help\n" +
                "\tUsage with arguments:\n" +
                "\t<name>\n" +
                "\t\t - set author's name\n\n" +
                "\tUsage without arguments:\n" +
                "\t\t - get author's name\n";
    }

    @Override
    public String getHelpBrief() {
        return "View current or alter 'author' file's name field. See 'shy help author name'";
    }
}
