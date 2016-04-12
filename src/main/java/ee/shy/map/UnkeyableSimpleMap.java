package ee.shy.map;

import java.io.IOException;

/**
 * Interface for simplified {@link java.util.Map} types with unpickable key insertion.
 * The significantly reduced method set simplifies implementation
 * while keeping all basic functionality.
 * @param <K> type of keys in this map
 * @param <V> type of values in this map
 */
public interface UnkeyableSimpleMap<K, V> extends UnmodifiableSimpleMap<K, V> {
    /**
     * Adds the given value in this map.
     * @param value value to add
     * @return key for the value
     * @throws IOException if underlying I/O operation fails
     */
    K put(V value) throws IOException;
}
