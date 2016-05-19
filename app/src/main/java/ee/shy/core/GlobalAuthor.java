package ee.shy.core;

import ee.shy.io.Json;
import ee.shy.io.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GlobalAuthor {

    private static final Path configPath = PathUtils.getUserHomePath().resolve(".shyconfig");

    public GlobalAuthor() throws IOException {
        if (!Files.exists(configPath)) {
            Files.createFile(configPath);
            Json.write(configPath, new Author(null, null));
        }
    }

    public Author getAuthor() throws IOException {
        return Json.read(configPath, Author.class);
    }

    public String getGlobalEmail() throws IOException {
        return getAuthor().getEmail();
    }

    public String getGlobalName() throws IOException {
        return getAuthor().getName();
    }

    public void setGlobalEmail(String email) throws IOException {
        Json.write(configPath, new Author(getGlobalName(), email));
    }

    public void setGlobalName(String name) throws IOException {
        Json.write(configPath, new Author(name, getGlobalEmail()));
    }
}
