package ee.shy.storage;

/**
 * Class for decorating a value with its corresponding hash.
 * Replaces having to use a pair class repeatedly.
 * @param <T> type of value
 */
public class Hashed<T> {
    private final Hash hash;
    private final T value;

    public Hashed(Hash hash, T value) {
        this.hash = hash;
        this.value = value;
    }

    public Hash getHash() {
        return hash;
    }

    public T getValue() {
        return value;
    }
}
