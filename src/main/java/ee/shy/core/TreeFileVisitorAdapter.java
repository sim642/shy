package ee.shy.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TreeFileVisitorAdapter extends SimpleFileVisitor<Path> {
    private final Path rootPath;
    private final TreeVisitor treeVisitor;

    public TreeFileVisitorAdapter(Path rootPath, TreeVisitor treeVisitor) {
        this.rootPath = rootPath;
        this.treeVisitor = treeVisitor;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // TODO: 18.04.16 implement
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path relative = rootPath.relativize(file);

        String prefixPath = (relative.getParent() != null ? "/" + relative.getParent().toString() : "") + "/";
        String name = relative.getFileName().toString();

        try (InputStream is = Files.newInputStream(file)) {
            treeVisitor.visitFile(prefixPath, name, is);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        // TODO: 18.04.16 implement
        return FileVisitResult.CONTINUE;
    }
}
