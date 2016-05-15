package ee.shy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CollectionUtils {
    private CollectionUtils() {

    }

    public static <T> List<T> prependAll(List<T> list, T... items) {
        ArrayList<T> newList = new ArrayList<>(list);
        newList.addAll(0, Arrays.asList(items));
        return newList;
    }

    public static <T> List<T> appendAll(List<T> list, T... items) {
        ArrayList<T> newList = new ArrayList<>(list);
        newList.addAll(Arrays.asList(items));
        return newList;
    }

    /**
     * Tests if the array starts with another prefix array.
     * @param arr array to check prefix in
     * @param prefix prefix array
     * @param <T> type of array elements
     * @return {@code true} if array starts with prefix, {@code false} otherwise
     */
    public static <T> boolean startsWith(T[] arr, T[] prefix) {
        if (arr.length < prefix.length)
            return false;

        for (int i = 0; i < prefix.length; i++) {
            if (!arr[i].equals(prefix[i]))
                return false;
        }

        return true;
    }

    /**
     * Compares two arrays of comparable elements lexicographically.
     * @param arr1 first array
     * @param arr2 second array
     * @param <T> comparable type of array elements
     * @return negative integer if {@code arr1} comes before {@code arr2},
     *         positive integer if {@code arr1} comes after {@code arr2},
     *         {@code 0} if arrays equal
     */
    public static <T extends Comparable<T>> int compare(T[] arr1, T[] arr2) {
        int length = Math.min(arr1.length, arr2.length);
        for (int i = 0; i < length; i++) {
            int compare = arr1[i].compareTo(arr2[i]);
            if (compare != 0)
                return compare;
        }
        return arr1.length - arr2.length;
    }
}
