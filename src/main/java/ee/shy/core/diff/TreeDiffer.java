package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.storage.DataStorage;
import org.apache.commons.io.input.ClosedInputStream;

import java.io.IOException;
import java.util.*;

public class TreeDiffer implements Differ<Tree> {

    private final DataStorage storage;

    public TreeDiffer(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<String> diff(Tree original, Tree revised) throws IOException {
        List<String> diffStrings = new ArrayList<>();
        Map<String, TreeItem> originalItems = original.getItems();
        Map<String, TreeItem> revisedItems = revised.getItems();

        Set<String> unionTreeKeySet = new HashSet<>();
        unionTreeKeySet.addAll(originalItems.keySet());
        unionTreeKeySet.addAll(revisedItems.keySet());

        Map<ItemCase, ItemCaseHandler> itemCases = new HashMap<>();

        itemCases.put(new ItemCase(TreeItem.Type.FILE, TreeItem.Type.FILE),
                (originalItem, revisedItem) ->
                        new InputStreamDiffer().diff(storage.get(originalItem.getHash()), storage.get(revisedItem.getHash())));

        itemCases.put(new ItemCase(null, TreeItem.Type.FILE),
                (originalItem, revisedItem) ->
                        new InputStreamDiffer().diff(new ClosedInputStream(), storage.get(revisedItem.getHash())));

        itemCases.put(new ItemCase(TreeItem.Type.FILE, null),
                (originalItem, revisedItem) ->
                        new InputStreamDiffer().diff(storage.get(originalItem.getHash()), new ClosedInputStream()));

        for (String name : unionTreeKeySet) {
            TreeItem originalItem = originalItems.get(name);
            TreeItem revisedItem = revisedItems.get(name);
            ItemCase itemCase = new ItemCase(
                    originalItem != null ? originalItem.getType() : null,
                    revisedItem != null ? revisedItem.getType() : null
            );
            ItemCaseHandler treeItemTreeItemListBiFunction = itemCases.get(itemCase);
            if (treeItemTreeItemListBiFunction != null) {
                List<String> singleDiffStrings = treeItemTreeItemListBiFunction.handle(originalItem, revisedItem);
                diffStrings.addAll(singleDiffStrings);
            }
        }
        return diffStrings;
    }

    private static class ItemCase {
        private final TreeItem.Type originalType;
        private final TreeItem.Type revisedType;

        private ItemCase(TreeItem.Type originalType, TreeItem.Type revisedType) {
            this.originalType = originalType;
            this.revisedType = revisedType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            ItemCase itemCase = (ItemCase) o;

            if (originalType != itemCase.originalType)
                return false;
            return revisedType == itemCase.revisedType;
        }

        @Override
        public int hashCode() {
            int result = originalType != null ? originalType.hashCode() : 0;
            result = 31 * result + (revisedType != null ? revisedType.hashCode() : 0);
            return result;
        }
    }

    private interface ItemCaseHandler {
        List<String> handle(TreeItem originalItem, TreeItem revisedItem) throws IOException;
    }
}
