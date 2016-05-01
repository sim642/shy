package ee.shy.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Tree visitor implementation which supplies {@link Path}s for each item instead of prefix path and name.
 */
public abstract class PathTreeVisitor implements TreeVisitor {
    /**
     * Root path against which tree paths are resolved.
     */
    private final Path rootPath;

    /**
     * Constructs a new path tree visitor with given root path.
     * @param rootPath root path against which to resolve tree paths
     */
    protected PathTreeVisitor(Path rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * Invoked before a tree's items are visited.
     * @param directory path corresponding to tree being visited
     * @throws IOException if an I/O error occurs
     */
    protected void preVisitTree(Path directory) throws IOException {

    }

    @Override
    public void preVisitTree(String prefixPath, String name, Tree tree) throws IOException {
        preVisitTree(toPath(prefixPath, name));
    }

    /**
     * Invoked for each file in a tree.
     * @param file path corresponding to file being visited
     * @throws IOException if an I/O error occurs
     */
    protected void visitFile(Path file, InputStream is) throws IOException {

    }

    @Override
    public void visitFile(String prefixPath, String name, InputStream is) throws IOException {
        visitFile(toPath(prefixPath, name), is);
    }

    /**
     * Invoked after a tree's items are visited.
     * @param directory path corresponding to tree being visited
     * @throws IOException if an I/O error occurs
     */
    protected void postVisitTree(Path directory) throws IOException {

    }

    @Override
    public void postVisitTree(String prefixPath, String name, Tree tree) throws IOException {
        postVisitTree(toPath(prefixPath, name));
    }

    /**
     * Resolves prefix path and name against root path.
     * @param prefixPath prefix path from {@link TreeVisitor}
     * @param name name from {@link TreeVisitor}
     * @return path of given prefix path and name
     */
    private Path toPath(String prefixPath, String name) {
        if (prefixPath == null)
            return rootPath;
        else
            return rootPath.resolve(prefixPath.substring(1)).resolve(name);
    }
}
