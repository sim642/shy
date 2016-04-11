package ee.shy.core;

import com.google.gson.annotations.SerializedName;
import ee.shy.storage.Hash;

/**
 * Class representing an item in a {@link Tree}: a file or another {@link Tree}.
 */
public class TreeItem {
    /**
     * Enum of all item types.
     */
    public enum Type {
        /**
         * File item.
         */
        @SerializedName("file")
        FILE,

        /**
         * Tree item.
         */
        @SerializedName("tree")
        TREE,
    }

    /**
     * Tree item type (<code>file</code> or <code>tree</code>).
     */
    private final Type type;

    /**
     * Tree item hash.
     */
    private final Hash hash;

    public TreeItem(Type type, Hash hash) {
        this.type = type;
        this.hash = hash;
    }

    public Type getType() {
        return type;
    }

    public Hash getHash() {
        return hash;
    }
}
