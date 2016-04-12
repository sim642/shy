package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.io.Json;
import ee.shy.storage.DataStorage;
import org.apache.commons.io.input.ClosedInputStream;

import java.io.IOException;
import java.util.*;

public class TreeDiffer implements Differ<Tree> {

    private final DataStorage storage;
    private final Map<ItemCase, ItemCaseHandler> itemCases = new HashMap<>();
    private static final InputStreamDiffer inputStreamDiffer = new InputStreamDiffer();

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

        itemCases.put(new ItemCase(TreeItem.Type.FILE, TreeItem.Type.FILE),
                (originalItem, revisedItem) ->
                        inputStreamDiffer.diff(storage.get(originalItem.getHash()), storage.get(revisedItem.getHash())));

        itemCases.put(new ItemCase(null, TreeItem.Type.FILE),
                (originalItem, revisedItem) ->
                        inputStreamDiffer.diff(ClosedInputStream.CLOSED_INPUT_STREAM, storage.get(revisedItem.getHash())));

        itemCases.put(new ItemCase(TreeItem.Type.FILE, null),
                (originalItem, revisedItem) ->
                        inputStreamDiffer.diff(storage.get(originalItem.getHash()), ClosedInputStream.CLOSED_INPUT_STREAM));

        itemCases.put(new ItemCase(TreeItem.Type.TREE, TreeItem.Type.TREE),
                (originalItem, revisedItem) -> diff(
                        Json.read(storage.get(originalItem.getHash()), Tree.class),
                        Json.read(storage.get(revisedItem.getHash()), Tree.class)
                ));

        itemCases.put(new ItemCase(TreeItem.Type.TREE, null),
                (originalItem, revisedItem) -> diff(
                        Json.read(storage.get(originalItem.getHash()), Tree.class),
                        Tree.EMPTY
                ));

        itemCases.put(new ItemCase(null, TreeItem.Type.TREE),
                (originalItem, revisedItem) -> diff(
                        Tree.EMPTY,
                        Json.read(storage.get(revisedItem.getHash()), Tree.class)
                ));

        ItemCaseHandler treeFileCase = (originalItem, revisedItem) -> {
            List<String> treeFileDiff = new ArrayList<>();
            treeFileDiff.addAll(itemCases.get(new ItemCase(originalItem.getType(), null)).handle(originalItem, revisedItem));
            treeFileDiff.addAll(itemCases.get(new ItemCase(null, revisedItem.getType())).handle(originalItem, revisedItem));
            return treeFileDiff;
        };

        itemCases.put(new ItemCase(TreeItem.Type.TREE, TreeItem.Type.FILE), treeFileCase);
        itemCases.put(new ItemCase(TreeItem.Type.FILE, TreeItem.Type.TREE), treeFileCase);

        for (String name : unionTreeKeySet) {
            TreeItem originalItem = originalItems.get(name);
            TreeItem revisedItem = revisedItems.get(name);
            ItemCase itemCase = new ItemCase(
                    originalItem != null ? originalItem.getType() : null,
                    revisedItem != null ? revisedItem.getType() : null
            );
            ItemCaseHandler itemCaseHandler = itemCases.get(itemCase);
            if (itemCaseHandler != null) {
                List<String> singleDiffStrings = itemCaseHandler.handle(originalItem, revisedItem);
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
