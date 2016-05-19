package ee.shy.core.merge;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.core.TreePath;
import ee.shy.core.diff.TreePairs;
import ee.shy.io.PathUtils;
import ee.shy.storage.DataStorage;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TreeMerger implements Merger<Tree> {
    private static final InputStreamMerger INPUT_STREAM_MERGER = new InputStreamMerger();

    private final DataStorage storage;

    public TreeMerger(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void merge(Path path, Tree original, Tree revised) throws IOException {
        TreePairs.visitPairs(storage, original, revised, new TreePairs.Visitor() {
            @Override
            public void visitFilePair(TreePath treePath, TreeItem lhs, TreeItem rhs) throws IOException {
                Path resolvedPath = resolveTreePath(path, treePath);

                InputStream leftStream = lhs != null ? storage.get(lhs.getHash()) : null;
                InputStream rightStream = rhs != null ? storage.get(rhs.getHash()) : null;

                INPUT_STREAM_MERGER.merge(resolvedPath, leftStream, rightStream);
            }

            @Override
            public void visitTreePair(TreePath treePath, TreeItem lhs, TreeItem rhs) throws IOException {
                Path resolvedPath = resolveTreePath(path, treePath);

                if (lhs == null) {
                    if (Files.notExists(resolvedPath))
                        Files.createDirectory(resolvedPath);
                }
                else if (rhs == null) {
                    if (Files.exists(resolvedPath))
                        PathUtils.deleteRecursive(path);
                }
            }
        });
    }

    private static Path resolveTreePath(Path path, TreePath treePath) {
        String[] pathStrings = treePath.getPathStrings();
        return path.resolve(Paths.get(pathStrings[0], ArrayUtils.remove(pathStrings, 0))); // FIXME: 17.05.16 better varargs construction
    }
}
