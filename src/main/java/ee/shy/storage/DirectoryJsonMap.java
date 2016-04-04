package ee.shy.storage;

import ee.shy.io.Json;
import ee.shy.io.Jsonable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Simple map for storing named objects as JSON in a directory.
 * @param <T> type of objects in this map
 */
public class DirectoryJsonMap<T extends Jsonable> implements NamedObjectMap<T> {
    /**
     * Class of objects.
     */
    private final Class<T> classofT;

    /**
     * Directory used for files with keys as their name.
     */
    private final Path directory;

    /**
     * Creates a new directory JSON map for objects of given class into given directory.
     * @param classofT class of objects
     * @param directory directory used for storing named JSON objects
     */
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
