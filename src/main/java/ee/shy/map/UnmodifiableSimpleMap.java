package ee.shy.map;

/**
 * Interface for simplified read-only {@link java.util.Map} types.
 * The significantly reduced method set simplifies implementation
 * while keeping most of basic functionality.
 * @param <K> type of keys in this map
 * @param <V> type of values in this map
 */
public interface UnmodifiableSimpleMap<K, V> {
    /**
     * Checks whether this map contains the given key.
     * @param key key which's containment to check
     * @return whether the key is contained in this map
     */
    default boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Gets the value for the given key in this map.
     * @param key key which's value to get
     * @return value for the given key
     */
    V get(K key);
}
