package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.storage.DataStorage;
import ee.shy.storage.Hash;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
        walk(storage, new TreeVisitor() {
            @Override
            public void visitFile(String prefixPath, String name, InputStream is) throws IOException {
                try (OutputStream os = Files.newOutputStream(toPath(prefixPath, name))) {
                    IOUtils.copy(is, os);
                }
            }

            @Override
            public void preVisitTree(String prefixPath, String name) throws IOException {
                if (prefixPath != null)
                    Files.createDirectories(toPath(prefixPath, name));
            }

            @Override
            public void postVisitTree(String prefixPath, String name) throws IOException {

            }

            private Path toPath(String prefixPath, String name) {
                return path.resolve(prefixPath.substring(1)).resolve(name);
            }
        });
    }

    public void walk(DataStorage storage, TreeVisitor visitor) throws IOException {
        walk(storage, visitor, null, "");
    }

    private void walk(DataStorage storage, TreeVisitor visitor, String prefixPath, String name) throws IOException {
        visitor.preVisitTree(prefixPath, name);

        String newPrefixPath = prefixPath != null ? prefixPath : "";
        newPrefixPath += name + "/";

        for (Map.Entry<String, TreeItem> entry : items.entrySet()) {
            switch (entry.getValue().getType()) {
                case FILE:
                    try (InputStream is = storage.get(entry.getValue().getHash())) {
                        visitor.visitFile(newPrefixPath, entry.getKey(), is);
                    }
                    break;

                case TREE:
                    Tree tree = storage.get(entry.getValue().getHash(), Tree.class);
                    tree.walk(storage, visitor, newPrefixPath, entry.getKey());
                    break;
            }
        }

        visitor.postVisitTree(prefixPath, name);
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
