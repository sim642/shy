package ee.shy.storage;

import ee.shy.DecoratedValue;

/**
 * Class for decorating a value with its corresponding hash.
 * @param <T> type of value
 */
public class Hashed<T> extends DecoratedValue<Hash, T> {
    public Hashed(Hash decoration, T value) {
        super(decoration, value);
    }

    public Hash getHash() {
        return getDecoration();
    }
}
