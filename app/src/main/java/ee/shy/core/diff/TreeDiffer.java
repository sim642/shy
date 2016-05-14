package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.storage.DataStorage;

import java.io.IOException;
import java.util.*;

/**
 * Class to get the differences between two tree objects.
 */
public class TreeDiffer implements Differ<Tree> {
    /**
     * Storage object to get stored items according to their hash values.
     */
    private final DataStorage storage;

    /**
     * Differ used for individual Tree items.
     */
    private final TreeItemDiffer treeItemDiffer;

    /**
     * Construct a new {@link TreeDiffer} with given {@link DataStorage} object.
     * @param storage storage to use
     */
    public TreeDiffer(DataStorage storage) {
        this.storage = storage;
        treeItemDiffer = new TreeItemDiffer(this);
    }

    @Override
    public List<String> diff(Tree original, Tree revised) throws IOException {
        return diff("", original, revised);
    }

    @Override
    public List<String> diff(String prefixPath, Tree original, Tree revised) throws IOException {
        List<String> diffStrings = new ArrayList<>();
        Map<String, TreeItem> originalItems = original.getItems();
        Map<String, TreeItem> revisedItems = revised.getItems();

        Set<String> unionTreeKeySet = new TreeSet<>();
        unionTreeKeySet.addAll(originalItems.keySet());
        unionTreeKeySet.addAll(revisedItems.keySet());

        for (String name : unionTreeKeySet) {
            TreeItem originalItem = originalItems.get(name);
            TreeItem revisedItem = revisedItems.get(name);

            List<String> currentDiffStrings = treeItemDiffer.diff(prefixPath + "/" + name, originalItem, revisedItem);

            diffStrings.addAll(currentDiffStrings);
            if (!currentDiffStrings.isEmpty())
                diffStrings.add("");
        }
        if (!diffStrings.isEmpty())
            diffStrings.remove(diffStrings.size() - 1);
        return diffStrings;
    }

    public DataStorage getStorage() {
        return storage;
    }
}