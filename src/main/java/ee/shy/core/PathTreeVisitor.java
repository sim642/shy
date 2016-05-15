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
    public void preVisitTree(TreePath path) throws IOException {
        preVisitTree(toPath(path));
    }

    /**
     * Invoked for each file in a tree.
     * @param file path corresponding to file being visited
     * @throws IOException if an I/O error occurs
     */
    protected void visitFile(Path file, InputStream is) throws IOException {

    }

    @Override
    public void visitFile(TreePath path, InputStream is) throws IOException {
        visitFile(toPath(path), is);
    }

    /**
     * Invoked after a tree's items are visited.
     * @param directory path corresponding to tree being visited
     * @throws IOException if an I/O error occurs
     */
    protected void postVisitTree(Path directory) throws IOException {

    }

    @Override
    public void postVisitTree(TreePath path) throws IOException {
        postVisitTree(toPath(path));
    }

    /**
     * Resolves prefix path and name against root path.
     * @param path prefix path from {@link TreeVisitor}
     * @return path of given prefix path and name
     */
    private Path toPath(TreePath path) {
        return rootPath.resolve(path.toString().substring(1));
    }
}
