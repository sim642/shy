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
}
