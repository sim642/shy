package ee.shy.core.merge;

import difflib.PatchFailedException;

import java.io.IOException;

/**
 * Interface for implementing merge functionality for different types.
 * @param <T> type to merge
 */
public interface Merger<T> {
    /**
     * Three-way merge on original, revised and patchable inputs.
     * Return the result as a T type.
     *
     * @param original
     * @param revised
     * @param patchable
     * @return merged result
     * @throws IOException
     * @throws PatchFailedException If an merge confict occurs
     */
    T merge(T original, T revised, T patchable) throws IOException, PatchFailedException;
}