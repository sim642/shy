package ee.shy.cli.author;

import ee.shy.cli.Command;
import ee.shy.core.Author;
import ee.shy.core.Repository;
import ee.shy.io.Json;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Command to get or set the reposirory's author's email.
 */
public class EmailCommand implements Command {

    @Override
    public void execute(String[] args) throws IOException {
        Repository repository = Repository.newExisting();

        if (args.length == 0) {
            FileInputStream fis = new FileInputStream(repository.getAuthor());
            Author author = Json.read(fis, Author.class);
            if (!author.getEmail().equals("")) {
                System.out.println(author.getEmail());
            } else {
                System.out.println("Email not set. See 'shy author help'");
            }
        } else {
            FileInputStream fis = new FileInputStream(repository.getAuthor());
            Author author = Json.read(fis, Author.class);
            author.setEmail(args[0]);
            author.write(new FileOutputStream(repository.getAuthor()));
            System.out.println("Set repository email to: " + args[0]);
        }
    }

    @Override
    public String getHelp() {
        return "EmailCommand help!";
    }
}
