package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.storage.DataStorage;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

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

        Map<ItemCase, BiFunction<TreeItem, TreeItem, List<String>>> itemCases = new HashMap<>();

        itemCases.put(new ItemCase(true, true, TreeItem.Type.FILE, TreeItem.Type.FILE),
                (originalItem, revisedItem) -> {
                    try {
                        return new InputStreamDiffer().diff(
                                storage.get(originalItem.getHash()),
                                storage.get(revisedItem.getHash())
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        for (String name : unionTreeKeySet) {
            ItemCase itemCase = new ItemCase(
                    originalItems.containsKey(name),
                    revisedItems.containsKey(name),
                    originalItems.get(name).getType(),
                    revisedItems.get(name).getType()
            );
            BiFunction<TreeItem, TreeItem, List<String>> treeItemTreeItemListBiFunction = itemCases.get(itemCase);
            if (treeItemTreeItemListBiFunction != null) {
                List<String> singleDiffStrings = treeItemTreeItemListBiFunction.apply(originalItems.get(name), revisedItems.get(name));
                diffStrings.addAll(singleDiffStrings);
            }
        }
        return diffStrings;
    }

    private static class ItemCase {
        private final boolean originalContains;
        private final boolean revisedContains;
        private final TreeItem.Type originalType;
        private final TreeItem.Type revisedType;

        private ItemCase(boolean originalContains, boolean revisedContains, TreeItem.Type originalType, TreeItem.Type revisedType) {
            this.originalContains = originalContains;
            this.revisedContains = revisedContains;
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

            if (originalContains != itemCase.originalContains)
                return false;
            if (revisedContains != itemCase.revisedContains)
                return false;
            if (originalType != itemCase.originalType)
                return false;
            return revisedType == itemCase.revisedType;

        }

        @Override
        public int hashCode() {
            int result = (originalContains ? 1 : 0);
            result = 31 * result + (revisedContains ? 1 : 0);
            result = 31 * result + originalType.hashCode();
            result = 31 * result + revisedType.hashCode();
            return result;
        }
    }

}
