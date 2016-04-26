package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.storage.DataStorage;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to get the differences between two tree objects.
 */
public class TreeDiffer implements Differ<Tree> {
    private static final String NAME_ADD = "+++ ";
    private static final String NAME_REMOVE = "--- ";

    /**
     * Differ to use for diffing two input streams.
     */
    private static final InputStreamDiffer inputStreamDiffer = new InputStreamDiffer();

    /**
     * Storage object to get stored items according to their hash values.
     */
    private final DataStorage storage;

    /**
     * Construct a new {@link TreeDiffer} with given {@link DataStorage} object.
     * @param storage storage to use
     */
    public TreeDiffer(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<String> diff(Tree original, Tree revised) throws IOException {
        List<String> diffLines = new ArrayList<>();

        new TreePairVisitor(storage) {
            private int linesBeforeItem;

            @Override
            public void preVisitItem(String prefixPath, String name) throws IOException {
                linesBeforeItem = diffLines.size();
            }

            @Override
            public void postVisitItem(String prefixPath, String name) throws IOException {
                if (diffLines.size() != linesBeforeItem)
                    diffLines.add("");
            }

            @Override
            public void postVisitTree(String prefixPath, String name) throws IOException {
                while (diffLines.size() > 0 && diffLines.get(diffLines.size() - 1).isEmpty())
                    diffLines.remove(diffLines.size() - 1);
            }

            @Override
            public void visitPair(String prefixPath, String name, InputStream lhs, InputStream rhs) throws IOException {
                List<String> diff = inputStreamDiffer.diff(lhs, rhs);
                if (!diff.isEmpty())
                    diffLines.addAll(Arrays.asList(NAME_REMOVE + prefixPath + name, NAME_ADD + prefixPath + name));
                diffLines.addAll(diff);
            }

            @Override
            public void visitPair(String prefixPath, String name, ObjectUtils.Null lhs, InputStream rhs) throws IOException {
                diffLines.add(NAME_ADD + prefixPath + name);
                diffLines.addAll(inputStreamDiffer.diff(ClosedInputStream.CLOSED_INPUT_STREAM, rhs));
            }

            @Override
            public void visitPair(String prefixPath, String name, InputStream lhs, ObjectUtils.Null rhs) throws IOException {
                diffLines.add(NAME_REMOVE + prefixPath + name);
                diffLines.addAll(inputStreamDiffer.diff(lhs, ClosedInputStream.CLOSED_INPUT_STREAM));
            }

            @Override
            public void visitPair(String prefixPath, String name, ObjectUtils.Null lhs, Tree rhs) throws IOException {
                diffLines.add(NAME_ADD + prefixPath + name + "/");
                diffLines.add("");
                super.visitPair(prefixPath, name, lhs, rhs);
            }

            @Override
            public void visitPair(String prefixPath, String name, Tree lhs, ObjectUtils.Null rhs) throws IOException {
                diffLines.add(NAME_REMOVE + prefixPath + name + "/");
                diffLines.add("");
                super.visitPair(prefixPath, name, lhs, rhs);
            }

            @Override
            public void visitPair(String prefixPath, String name, InputStream lhs, Tree rhs) throws IOException {
                visitPair(prefixPath, name, lhs, ObjectUtils.NULL);
                diffLines.add("");
                visitPair(prefixPath, name, ObjectUtils.NULL, rhs);
            }

            @Override
            public void visitPair(String prefixPath, String name, Tree lhs, InputStream rhs) throws IOException {
                visitPair(prefixPath, name, lhs, ObjectUtils.NULL);
                diffLines.add("");
                visitPair(prefixPath, name, ObjectUtils.NULL, rhs);
            }
        }.walk(original, revised);

        return diffLines;
    }
}
