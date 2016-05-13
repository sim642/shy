package ee.shy;

import org.apache.commons.lang3.StringUtils;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * {@link org.junit.Rule} for accessing resources in {@link Path} manner.
 */
public class ResourcePaths extends ExternalResource {
    /**
     * Class in which's package to find the resources.
     */
    private final Class classofT;

    /**
     * FileSystem to access resources through.
     */
    private FileSystem fileSystem;

    /**
     * Root path for resources in {@link #fileSystem}.
     */
    private Path rootPath;

    /**
     * Constructs a new resource path provider for given class.
     * @param classofT class to create provider for
     * @param <T> type of class
     */
    public <T> ResourcePaths(Class<T> classofT) {
        this.classofT = classofT;
    }

    @Override
    protected void before() throws URISyntaxException, IOException {
        URI resourceURI = classofT.getResource("").toURI();

        switch (resourceURI.getScheme()) {
            case "file":
                fileSystem = FileSystems.getDefault();
                rootPath = Paths.get(resourceURI);
                break;

            case "jar":
                // TODO: 16.04.16 test jar case
                // http://stackoverflow.com/a/15718001/854540
                String[] pieces = StringUtils.splitByWholeSeparator(resourceURI.toString(), "!/", 2);

                fileSystem = FileSystems.newFileSystem(URI.create(pieces[0]), Collections.emptyMap());
                rootPath = fileSystem.getPath(pieces[1]);
                break;

            default:
                throw new RuntimeException("resources in unknown scheme");
        }
    }

    @Override
    protected void after() {
        try {
            if (!fileSystem.equals(FileSystems.getDefault()))
                fileSystem.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns path for resource by name.
     * @param name name of resource
     * @return path for resource
     */
    public Path get(String name) {
        return rootPath.resolve(name);
    }

    /**
     * Returns path for resource by name in static context.
     * @param classofT class which's package to find resources in
     * @param name name of resource
     * @param <T> type of class
     * @return path for resource
     * @throws IOException
     * @throws URISyntaxException
     */
    public static <T> Path getPath(Class<T> classofT, String name) throws IOException, URISyntaxException {
        ResourcePaths resourcePaths = new ResourcePaths(classofT);
        try {
            resourcePaths.before();
            return resourcePaths.get(name);
        }
        finally {
            resourcePaths.after();
        }
    }
}
