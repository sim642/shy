package ee.shy.core;

import com.google.gson.internal.LinkedTreeMap;
import ee.shy.io.Jsonable;

import java.util.Map;

public class Tree extends Jsonable {
    private final Map<String, TreeItem> items;

    public Tree() {
        items = new LinkedTreeMap<>();
    }
}
