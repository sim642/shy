package ee.shy.map;

import ee.shy.DecoratedValue;

/**
 * Class for decorating a value with its name.
 * @param <T> type of value
 */
public class Named<T> extends DecoratedValue<String, T> {
    public Named(String decoration, T value) {
        super(decoration, value);
    }

    public String getName() {
        return getDecoration();
    }
}
