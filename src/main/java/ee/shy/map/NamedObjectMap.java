package ee.shy.map;

import java.io.IOException;
import java.util.Set;

/**
 * Interface for simplified {@link java.util.Map} for {@link String} keys.
 * @param <T> type of values in this map
 */
public interface NamedObjectMap<T> extends KeyableSimpleMap<String, T> {
    /**
     * Returns a {@link Set} of named values contained in this map.
     * @return a set of the named values contained in this map
     * @throws IOException if underlying I/O operation fails
     */
    Set<Named<T>> entrySet() throws IOException;
}
