package ee.shy.cli.command.author;

import ee.shy.cli.Command;
import ee.shy.core.Author;
import ee.shy.io.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GlobalEmailCommand implements Command {
    @Override
    public void execute(String[] args) throws IOException {
        Path configPath = Paths.get(System.getProperty("user.home")).resolve(".shyconfig");
        if (!Files.exists(configPath)) {
            configPath.toFile().createNewFile();
            Json.write(configPath, new Author(null, null));
        }
        Author author = Json.read(configPath, Author.class);
        Author newAuthor = new Author(author.getName(), args[0]);
        Json.write(configPath, newAuthor);
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getHelpBrief() {
        return null;
    }
}
