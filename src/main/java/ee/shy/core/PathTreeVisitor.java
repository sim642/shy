package ee.shy.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class PathTreeVisitor implements TreeVisitor {
    private final Path rootPath;

    protected PathTreeVisitor(Path rootPath) {
        this.rootPath = rootPath;
    }

    protected abstract void visitFile(Path file, InputStream is) throws IOException;

    @Override
    public void visitFile(String prefixPath, String name, InputStream is) throws IOException {
        visitFile(toPath(prefixPath, name), is);
    }

    protected abstract void preVisitTree(Path directory) throws IOException;

    @Override
    public void preVisitTree(String prefixPath, String name) throws IOException {
        preVisitTree(toPath(prefixPath, name));
    }

    protected abstract void postVisitTree(Path directory) throws IOException;

    @Override
    public void postVisitTree(String prefixPath, String name) throws IOException {
        postVisitTree(toPath(prefixPath, name));
    }

    private Path toPath(String prefixPath, String name) {
        if (prefixPath == null)
            return rootPath;
        else
            return rootPath.resolve(prefixPath.substring(1)).resolve(name);
    }
}
