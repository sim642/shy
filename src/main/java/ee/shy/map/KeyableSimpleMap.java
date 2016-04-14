package ee.shy.map;

import java.io.IOException;
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
     * @throws IOException if underlying I/O operation fails
     */
    void put(K key, V value) throws IOException;

    /**
     * Removes the given key from this map.
     * @param key key to remove
     * @throws IOException if underlying I/O operation fails
     */
    void remove(K key) throws IOException;

    /**
     * Returns a {@link Set} of the keys contained in this map.
     * @return a set of the keys contained in this map
     * @throws IOException if underlying I/O operation fails
     */
    Set<K> keySet() throws IOException;
}
