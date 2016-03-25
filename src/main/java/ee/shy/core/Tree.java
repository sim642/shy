package ee.shy.core;

import com.google.gson.internal.LinkedTreeMap;
import ee.shy.io.Jsonable;

import java.util.Map;

/**
 * Class representing a directory tree.
 */
public class Tree extends Jsonable {
    /**
     * Mapping of names to {@link TreeItem}s.
     */
    private final Map<String, TreeItem> items;

    public Tree() {
        items = new LinkedTreeMap<>();
    }
}
