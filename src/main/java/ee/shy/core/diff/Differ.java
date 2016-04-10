package ee.shy.core.diff;

import java.io.IOException;
import java.util.List;

/**
 * Interface for implementing diff functionality for different types.
 * @param <T> type to diff
 */
public interface Differ<T> {

    /**
     * Get the differences of two given objects with type T as a String List.
     * List elements starting with character '+' indicate additions to the original object.
     * List elements starting with character '-' indicate deletions from the original object.
     * @param original original object
     * @param revised revised object
     * @return String list containing differences.
     * @throws IOException
     */
    List<String> diff(T original, T revised) throws IOException;
}
