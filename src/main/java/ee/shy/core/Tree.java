package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.DataStorage;
import ee.shy.storage.Hash;
import org.apache.commons.io.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

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

    public void toDirectory(Path path, DataStorage storage) throws IOException {
        for (Map.Entry<String, TreeItem> entry : items.entrySet()) {
            switch (entry.getValue().getType()) {
                case FILE:
                    InputStream is = storage.get(entry.getValue().getHash());
                    OutputStream os = Files.newOutputStream(path.resolve(entry.getKey()));
                    IOUtils.copy(is, os);
                    break;

                case TREE:
                    Files.createDirectory(path.resolve(entry.getKey()));
                    Tree tree = storage.get(entry.getValue().getHash(), Tree.class);
                    tree.toDirectory(path.resolve(entry.getKey()), storage);
                    break;
            }
        }
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
            try (DirectoryStream<Path> fileStream = Files.newDirectoryStream(directory)) {
                for (Path file : fileStream) {
                    if (Files.isRegularFile(file)) {
                        Hash hash;
                        try (InputStream inputStream = Files.newInputStream(file)) {
                            hash = storage.put(inputStream);
                        }
                        addItem(file.getFileName().toString(), new TreeItem(TreeItem.Type.FILE, hash));
                    }
                    else if (Files.isDirectory(file)) {
                        Tree tree = new Builder(storage).fromDirectory(file).create();
                        Hash hash = storage.put(tree);
                        addItem(file.getFileName().toString(), new TreeItem(TreeItem.Type.TREE, hash));
                    }
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
