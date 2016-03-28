package ee.shy;

import java.util.Set;

/**
 * Interface for simplified {@link java.util.Map} types with given key insertion.
 * The significantly reduced method set simplifies implementation
 * while keeping all basic functionality.
 * @param <K> type of keys in this map
 * @param <V> type of values in this map
 */
public interface KeyableSimpleMap<K, V> extends UnmodifiableSimpleMap<K, V> {
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
