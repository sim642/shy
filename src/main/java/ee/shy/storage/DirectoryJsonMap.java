package ee.shy.storage;

import ee.shy.io.Json;
import ee.shy.io.Jsonable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class DirectoryJsonMap<T extends Jsonable> implements NamedObjectMap<T> {
    private final Class<T> classofT;
    private final Path directory;

    public DirectoryJsonMap(Class<T> classofT, Path directory) {
        this.classofT = classofT;
        this.directory = directory;
    }

    @Override
    public void put(String key, T value) {
        try {
            value.write(Files.newOutputStream(directory.resolve(key)));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            Files.delete(directory.resolve(key));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> keySet() {
        try {
            return Files.list(directory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T get(String key) {
        try {
            return Json.read(Files.newInputStream(directory.resolve(key)), classofT);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
