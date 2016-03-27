package ee.shy;

import java.util.Set;

/**
 * Interface for simplified {@link java.util.Map} types.
 * The significantly reduced method set simplifies implementation
 * while keeping all basic functionality.
 * @param <K> type of keys in this map
 * @param <V> type of values in this map
 */
public interface SimpleMap<K, V> {
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

    /**
     * Adds the given value with the given key in this map.
     * @param key key for the value
     * @param value value to add
     */
    void put(K key, V value);

    /**
     * Returns a {@link Set} of the keys contained in this map.
     * @return a set of the keys contained in this map
     */
    Set<K> keySet();
}
