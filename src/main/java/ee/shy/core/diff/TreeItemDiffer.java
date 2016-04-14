package ee.shy.core.diff;

import ee.shy.CollectionUtils;
import ee.shy.core.Tree;
import ee.shy.core.TreeItem;
import ee.shy.storage.DataStorage;
import org.apache.commons.io.input.ClosedInputStream;

import java.io.IOException;
import java.util.*;

/**
 * Class to get the differences between two TreeItem objects.
 */
public class TreeItemDiffer implements Differ<TreeItem> {
    private static final String NAME_ADD = "+++ ";
    private static final String NAME_REMOVE = "--- ";

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
                (name, originalItem, revisedItem) -> {
                        List<String> diff = inputStreamDiffer.diff(storage.get(originalItem.getHash()), storage.get(revisedItem.getHash()));
                        if (!diff.isEmpty())
                            diff.addAll(0, Arrays.asList(NAME_REMOVE + name, NAME_ADD + name));
                        return diff;
                });

        itemCases.put(new ItemCase(null, TreeItem.Type.FILE),
                (name, originalItem, revisedItem) ->
                        CollectionUtils.prependAll(
                                inputStreamDiffer.diff(ClosedInputStream.CLOSED_INPUT_STREAM, storage.get(revisedItem.getHash())),
                                NAME_ADD + name
                        ));

        itemCases.put(new ItemCase(TreeItem.Type.FILE, null),
                (name, originalItem, revisedItem) ->
                        CollectionUtils.prependAll(
                                inputStreamDiffer.diff(storage.get(originalItem.getHash()), ClosedInputStream.CLOSED_INPUT_STREAM),
                                NAME_REMOVE + name
                        ));

        itemCases.put(new ItemCase(TreeItem.Type.TREE, TreeItem.Type.TREE),
                (name, originalItem, revisedItem) ->
                        treeDiffer.diff(
                                name,
                                storage.get(originalItem.getHash(), Tree.class),
                                storage.get(revisedItem.getHash(), Tree.class))
                );

        itemCases.put(new ItemCase(TreeItem.Type.TREE, null),
                (name, originalItem, revisedItem) ->
                        CollectionUtils.prependAll(
                                treeDiffer.diff(
                                        name,
                                        storage.get(originalItem.getHash(), Tree.class),
                                        Tree.EMPTY),
                                NAME_REMOVE + name, ""
                        ));

        itemCases.put(new ItemCase(null, TreeItem.Type.TREE),
                (name, originalItem, revisedItem) ->
                        CollectionUtils.prependAll(
                                treeDiffer.diff(
                                        name,
                                        Tree.EMPTY,
                                        storage.get(revisedItem.getHash(), Tree.class)),
                                NAME_ADD + name, ""
                ));

        Differ<TreeItem> treeFileCase = (name, originalItem, revisedItem) -> {
            List<String> treeFileDiff = new ArrayList<>();
            treeFileDiff.addAll(itemCases.get(new ItemCase(originalItem.getType(), null)).diff(name, originalItem, revisedItem));
            treeFileDiff.addAll(itemCases.get(new ItemCase(null, revisedItem.getType())).diff(name, originalItem, revisedItem));
            return CollectionUtils.prependAll(treeFileDiff, NAME_REMOVE + name, NAME_ADD + name);
        };

        itemCases.put(new ItemCase(TreeItem.Type.TREE, TreeItem.Type.FILE), treeFileCase);
        itemCases.put(new ItemCase(TreeItem.Type.FILE, TreeItem.Type.TREE), treeFileCase);
    }

    @Override
    public List<String> diff(String name, TreeItem original, TreeItem revised) throws IOException {
        ItemCase itemCase = new ItemCase(
                original != null ? original.getType() : null,
                revised != null ? revised.getType() : null
        );

        Differ<TreeItem> itemCaseHandler = itemCases.get(itemCase);
        if (itemCaseHandler == null)
            throw new RuntimeException("Item case handler not found (" + itemCase + ")");

        return itemCaseHandler.diff(name, original, revised);
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
