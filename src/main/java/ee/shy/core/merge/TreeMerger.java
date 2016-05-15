package ee.shy.core.merge;

import difflib.PatchFailedException;
import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.core.TreePath;
import ee.shy.core.TreeVisitor;
import ee.shy.storage.DataStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TreeMerger implements Merger<Tree>{

    private static final InputStreamMerger inputStreamMerger = new InputStreamMerger();

    private final DataStorage storage;

    public TreeMerger(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public Tree merge(Tree original, Tree revised, Tree patchable) throws IOException, PatchFailedException {
        Tree.Builder mergedTreeBuilder = new Tree.Builder(storage);
        patchable.walk(storage, new TreeVisitor() {
            @Override
            public void visitFile(TreePath path, InputStream is) throws IOException {
                try {
                    mergedTreeBuilder.addItem(path.toString(), new TreeItem(TreeItem.Type.FILE,
                            storage.put(inputStreamMerger.merge(
                                    findItemFromTree(original, path),
                                    findItemFromTree(revised, path),
                                    is
                    ))));
                } catch (PatchFailedException e) {
                    System.err.println("Merge conflict!");
                    throw new RuntimeException(e);
                }
            }
        });
        return mergedTreeBuilder.create();
    }

    private InputStream findItemFromTree(Tree tree, TreePath searchable) throws IOException {
        for (Map.Entry<String, TreeItem> k : tree.getItems().entrySet()) {
            if (new TreePath().resolve(k.getKey()).compareTo(searchable) == 0) {
                return storage.get(k.getValue().getHash());
            }
            if (k.getValue().getType() == TreeItem.Type.TREE) {
                return findItemFromTree(storage.get(k.getValue().getHash(), Tree.class), searchable);
            }
        }
        return null;
    }
}
