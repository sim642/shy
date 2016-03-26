package ee.shy.cli.command.author;

import ee.shy.cli.Command;
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
        return "shy author email help\n" +
                "\tUsage: shy author email [new_value]\n\n" +
                "Description:\n" +
                "\tChange the repository's author's email to [new_value].\n" +
                "\tIf no [new_value] is given: return current author's email value.";
    }
}
