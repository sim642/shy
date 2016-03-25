package ee.shy.core;

import ee.shy.storage.Hash;

/**
 * Class representing an item in a {@link Tree}: a file or another {@link Tree}.
 */
public class TreeItem {
    /**
     * Tree item type (<code>file</code> or <code>tree</code>).
     */
    private final String type;

    /**
     * Tree item hash.
     */
    private final Hash hash;

    public TreeItem(String type, Hash hash) {
        this.type = type;
        this.hash = hash;
    }
}
