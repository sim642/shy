package ee.shy.core;

import ee.shy.storage.Hash;

public class TreeItem {
    private final String type;
    private final Hash hash;

    public TreeItem(String type, Hash hash) {
        this.type = type;
        this.hash = hash;
    }
}
