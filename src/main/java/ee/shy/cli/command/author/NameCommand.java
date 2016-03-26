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
                "\tUsage: shy author name [new_value]\n\n" +
                "Description:\n" +
                "\tChange the repository's author's name to [new_value].\n" +
                "\tIf no [new_value] is given: return current author's name value.";
    }
}
