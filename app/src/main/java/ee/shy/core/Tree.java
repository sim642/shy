package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.PathUtils;
import ee.shy.storage.DataStorage;
import ee.shy.storage.Hash;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing a directory tree.
 */
public class Tree implements Jsonable {
    /**
     * Empty tree constant.
     */
    public static final Tree EMPTY = new Tree(Collections.emptyMap());

    /**
     * Mapping of names to {@link TreeItem}s.
     */
    private final Map<String, TreeItem> items;

    /**
     * Constructs a tree from given items.
     * @param items map of items
     */
    public Tree(Map<String, TreeItem> items) {
        this.items = new TreeMap<>(items);
    }

    /**
     * Constructs a tree from its builder.
     * @param builder tree builder
     */
    public Tree(Builder builder) {
        this.items = new TreeMap<>(builder.items);
    }

    /**
     * Extracts files and directories from a tree to root directory
     * @param path path to extract to
     * @param storage data storage
     * @throws IOException if there was a problem with streams
     */
    public void toDirectory(Path path, DataStorage storage) throws IOException {
        walk(storage, new PathTreeVisitor(path) {
            @Override
            protected void visitFile(Path file, InputStream is) throws IOException {
                if (Files.isDirectory(file))
                    PathUtils.deleteRecursive(file);
                Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
            }

            @Override
            protected void preVisitTree(Path directory) throws IOException {
                if (Files.isRegularFile(directory))
                    Files.delete(directory);
                Files.createDirectories(directory);
            }
        });
    }

    public void walk(DataStorage storage, TreeVisitor visitor) throws IOException {
        walk(storage, visitor, new TreePath());
    }

    private void walk(DataStorage storage, TreeVisitor visitor, TreePath path) throws IOException {
        visitor.preVisitTree(path, this);

        for (Map.Entry<String, TreeItem> entry : items.entrySet()) {
            TreePath subPath = path.resolve(entry.getKey());

            switch (entry.getValue().getType()) {
                case FILE:
                    try (InputStream is = storage.get(entry.getValue().getHash())) {
                        visitor.visitFile(subPath, is);
                    }
                    break;

                case TREE:
                    Tree tree = storage.get(entry.getValue().getHash(), Tree.class);
                    tree.walk(storage, visitor, subPath);
                    break;
            }
        }

        visitor.postVisitTree(path, this);
    }

    /*
     * Get Tree's items as an unmodifiable Map.
     * @return tree's items
     */
    public Map<String, TreeItem> getItems() {
        return Collections.unmodifiableMap(items);
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
