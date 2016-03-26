package ee.shy.cli.author;

import ee.shy.cli.Command;
import ee.shy.core.Author;
import ee.shy.core.Repository;
import ee.shy.io.Json;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Command to set or get the repository's author's name.
 */
public class NameCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();

        if (args.length == 0) {
            FileInputStream fis = new FileInputStream(repository.getAuthor());
            Author author = Json.read(fis, Author.class);
            if (!author.getName().equals("")) {
                System.out.println(author.getName());
            } else {
                System.out.println("Name not set. See 'shy author help'");
            }
        } else {
            FileInputStream fis = new FileInputStream(repository.getAuthor());
            Author author = Json.read(fis, Author.class);
            author.setName(args[0]);
            author.write(new FileOutputStream(repository.getAuthor()));
            System.out.println("Set repository author to: " + args[0]);
        }
    }

    @Override
    public String getHelp() {
        return "'shy author name' help.";
    }
}
