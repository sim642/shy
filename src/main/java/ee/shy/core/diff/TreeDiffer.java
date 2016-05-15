package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.core.TreePath;
import ee.shy.storage.DataStorage;
import org.apache.commons.io.input.ClosedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TreeDiffer implements Differ<Tree> {

    private static final InputStreamDiffer inputStreamDiffer = new InputStreamDiffer();
    private static final String NAME_ADD = "+++ ";
    private static final String NAME_REMOVE = "--- ";

    private final DataStorage storage;

    public TreeDiffer(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<String> diff(Tree original, Tree revised) throws IOException {
        List<String> diffLines = new ArrayList<>();
        TreePairs.visitPairs(storage, original, revised, new TreePairs.Visitor() {
            @Override
            public void visitFilePair(TreePath path, TreeItem lhs, TreeItem rhs) throws IOException {
                InputStream leftStream = lhs != null ? storage.get(lhs.getHash()) : ClosedInputStream.CLOSED_INPUT_STREAM;
                InputStream rightStream = rhs != null ? storage.get(rhs.getHash()) : ClosedInputStream.CLOSED_INPUT_STREAM;
                List<String> diff = inputStreamDiffer.diff(leftStream, rightStream);
                if (lhs == null) {
                    diffLines.add(NAME_ADD + path);
                } else if (rhs == null) {
                    diffLines.add(NAME_REMOVE + path);
                } else if (!diff.isEmpty()) {
                    diffLines.add(NAME_REMOVE + path);
                    diffLines.add(NAME_ADD + path);
                }
                diffLines.addAll(diff);
                if (!diff.isEmpty())
                    diffLines.add("");
            }

            @Override
            public void visitTreePair(TreePath path, TreeItem lhs, TreeItem rhs) throws IOException {
                if (lhs == null) {
                    diffLines.add(NAME_ADD + path + "/");
                    diffLines.add("");
                }
                if (rhs == null) {
                    diffLines.add(NAME_REMOVE + path + "/");
                    diffLines.add("");
                }
            }
        });

        if (!diffLines.isEmpty())
            diffLines.remove(diffLines.size() - 1);
        return diffLines;
    }
}
