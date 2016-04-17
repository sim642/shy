package ee.shy.core;

import ee.shy.io.Jsonable;
import ee.shy.io.PathUtils;
import ee.shy.storage.DataStorage;
import ee.shy.storage.Hash;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;

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

    /**
     * Walks tree and tries to find matcher patterns from Tree files
     * @param matcher matcher with a pattern
     * @param path path to file
     * @param storage data storage
     * @return instances found in directory's files
     * @throws IOException when findInstance or storage.get() fails
     */
    public List<String> walkTreeAndFindInstances(Matcher matcher, String path, DataStorage storage) throws IOException {
        List<String> foundDirInstances = new ArrayList<>();
        for (Map.Entry<String, TreeItem> entry : items.entrySet()) {
            String newPath = path + "/" + entry.getKey();
            switch (entry.getValue().getType()) {
                case FILE:
                    List<String> foundFileInstances = findInstance(matcher, newPath, entry.getValue().getHash(), storage);
                    foundDirInstances.addAll(foundFileInstances);
                    break;

                case TREE:
                    Tree tree = storage.get(entry.getValue().getHash(), Tree.class);
                    List<String> foundBuffer = tree.walkTreeAndFindInstances(matcher, newPath, storage);
                    foundDirInstances.addAll(foundBuffer);
                    break;
            }
        }
        return foundDirInstances;
    }

    /**
     * Searches for an expression from file
     * @param matcher matcher
     * @param path path to file
     * @param hash file's hash
     * @param storage data storage
     * @return instances found in a file
     * @throws IOException if establishing streams fails
     */
    private List<String> findInstance(Matcher matcher, String path, Hash hash, DataStorage storage) throws IOException {
        List<String> foundInstances = new ArrayList<>();
        try (Reader reader = new InputStreamReader(storage.get(hash));
             LineNumberReader lineReader = new LineNumberReader(reader)) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                matcher.reset(line);
                if (matcher.find()) {
                    foundInstances.add(String.format("%s:%d[%d,%d]:%s",
                            path, lineReader.getLineNumber(), matcher.start(), matcher.end(), line));
                }
            }
        }
        return foundInstances;
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
