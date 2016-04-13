package ee.shy.core.diff;

import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.io.Json;
import ee.shy.storage.DataStorage;
import org.apache.commons.io.input.ClosedInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to get the differences between two TreeItem objects.
 */
public class TreeItemDiffer implements Differ<TreeItem> {
    /**
     * Storage object to get stored items according to their hash values.
     */
    private final DataStorage storage;

    /**
     * Map containing all the possible item cases and their corresponding handlers.
     */
    private final Map<ItemCase, Differ<TreeItem>> itemCases = new HashMap<>();

    /**
     * Owner of this TreeItemDiffer.
     */
    private final TreeDiffer treeDiffer;

    /**
     * InputStreamDiffer to differentiate input streams from FILE type tree items.
     */
    private static final InputStreamDiffer inputStreamDiffer = new InputStreamDiffer();

    public TreeItemDiffer(TreeDiffer treeDiffer) {
        this.treeDiffer = treeDiffer;
        this.storage = treeDiffer.getStorage();

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
                (originalItem, revisedItem) -> treeDiffer.diff(
                        Json.read(storage.get(originalItem.getHash()), Tree.class),
                        Json.read(storage.get(revisedItem.getHash()), Tree.class)
                ));

        itemCases.put(new ItemCase(TreeItem.Type.TREE, null),
                (originalItem, revisedItem) -> treeDiffer.diff(
                        Json.read(storage.get(originalItem.getHash()), Tree.class),
                        Tree.EMPTY
                ));

        itemCases.put(new ItemCase(null, TreeItem.Type.TREE),
                (originalItem, revisedItem) -> treeDiffer.diff(
                        Tree.EMPTY,
                        Json.read(storage.get(revisedItem.getHash()), Tree.class)
                ));

        Differ<TreeItem> treeFileCase = (originalItem, revisedItem) -> {
            List<String> treeFileDiff = new ArrayList<>();
            treeFileDiff.addAll(itemCases.get(new ItemCase(originalItem.getType(), null)).diff(originalItem, revisedItem));
            treeFileDiff.addAll(itemCases.get(new ItemCase(null, revisedItem.getType())).diff(originalItem, revisedItem));
            return treeFileDiff;
        };

        itemCases.put(new ItemCase(TreeItem.Type.TREE, TreeItem.Type.FILE), treeFileCase);
        itemCases.put(new ItemCase(TreeItem.Type.FILE, TreeItem.Type.TREE), treeFileCase);
    }

    @Override
    public List<String> diff(TreeItem original, TreeItem revised) throws IOException {
        ItemCase itemCase = new ItemCase(
                original != null ? original.getType() : null,
                revised != null ? revised.getType() : null
        );
        Differ<TreeItem> itemCaseHandler = itemCases.get(itemCase);
        if (itemCaseHandler != null) {
            // TODO: 13.04.16 Add file/directory names to the beginning of diff list.
            return itemCaseHandler.diff(original, revised);
        }
        throw new RuntimeException("Item case handler not found (" + itemCase + ")");
    }

    /**
     * Class to represent a specific case of all the possible cases.
     */
    private static class ItemCase {
        /**
         * Type of original {@link TreeItem}.
         */
        private final TreeItem.Type originalType;

        /**
         * Type of revised {@link TreeItem}.
         */
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

        @Override
        public String toString() {
            return originalType + ", " + revisedType;
        }
    }
}
