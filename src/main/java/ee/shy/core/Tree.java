package ee.shy.core;

import com.google.gson.internal.LinkedTreeMap;
import ee.shy.io.Jsonable;
import ee.shy.storage.DataStorage;
import ee.shy.storage.Hash;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Class representing a directory tree.
 */
public class Tree extends Jsonable {
    /**
     * Mapping of names to {@link TreeItem}s.
     */
    private final Map<String, TreeItem> items;

    /**
     * Constructs an empty tree.
     */
    public Tree() {
        this.items = new LinkedTreeMap<>();
    }

    /**
     * Constructs a tree from its builder.
     * @param builder tree builder
     */
    public Tree(Builder builder) {
        this.items = builder.items;
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
         * Mapping of names to {@link TreeItem}s.
         */
        private final Map<String, TreeItem> items;

        /**
         * Constructs a new tree builder with given data storage.
         * @param storage data storage to use
         */
        public Builder(DataStorage storage) {
            this.storage = storage;
            this.items = new LinkedTreeMap<>();
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
        public Builder fromDirectory(File directory) throws IOException {
            File[] files = directory.listFiles();
            if (files == null)
                throw new IOException("Attempted to build Tree from a non-directory");

            Arrays.sort(files); // guarantee stable order

            for (File file : files) {
                if (file.isFile()) {
                    Hash hash = storage.add(new FileInputStream(file));
                    addItem(file.getName(), new TreeItem(TreeItem.Type.FILE, hash));
                }
                else if (file.isDirectory()) {
                    Tree tree = new Builder(storage).fromDirectory(file).create();
                    Hash hash = storage.add(tree.inputify());
                    addItem(file.getName(), new TreeItem(TreeItem.Type.TREE, hash));
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
