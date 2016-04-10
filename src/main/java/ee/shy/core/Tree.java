package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.DataStorage;
import ee.shy.storage.Hash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing a directory tree.
 */
public class Tree implements Jsonable {
    /**
     * Mapping of names to {@link TreeItem}s.
     */
    private final Map<String, TreeItem> items;

    /**
     * Constructs a tree from its builder.
     * @param builder tree builder
     */
    public Tree(Builder builder) {
        this.items = new TreeMap<>(builder.items);
    }

    /**
     * Builder class for {@link Tree}.
     */
    public static class Builder implements ee.shy.Builder<Tree> {
        /**
         * Data storage to add files/trees to.
         */
        private final DataStorage storage;

        /**
         * @see Tree#items
         */
        private final Map<String, TreeItem> items;

        /**
         * Constructs a new tree builder with given data storage.
         * @param storage data storage to use
         */
        public Builder(DataStorage storage) {
            this.storage = storage;
            this.items = new TreeMap<>();
        }

        /**
         * Adds given tree item to tree being built with given name.
         * @param name name for tree item
         * @param item tree item to add
         * @return builder itself
         */
        public Builder addItem(String name, TreeItem item) {
            items.put(name, item);
            return this;
        }

        /**
         * Builds tree using given directory content and stores all referenced data.
         * @param directory directory to build tree from
         * @return builder itself
         * @throws IOException if there was a problem reading an addable file or tree to storage
         */
        public Builder fromDirectory(Path directory) throws IOException {
            List<Path> files = new ArrayList<>(Files.list(directory).collect(Collectors.toList())); // TODO: 6.04.16 Stream#toCollection for creating ArrayList

            Collections.sort(files);

            for (Path file : files) {
                if (Files.isRegularFile(file)) {
                    Hash hash = storage.put(Files.newInputStream(file));
                    addItem(file.getFileName().toString(), new TreeItem(TreeItem.Type.FILE, hash));
                }
                else if (Files.isDirectory(file)) {
                    Tree tree = new Builder(storage).fromDirectory(file).create();
                    Hash hash = storage.put(tree);
                    addItem(file.getFileName().toString(), new TreeItem(TreeItem.Type.TREE, hash));
                }
            }

            return this;
        }

        @Override
        public Tree create() {
            return new Tree(this);
        }
    }
}
