package ee.shy;

/**
 * Class for decorating a value with a decoration.
 * @param <D> type of decoration
 * @param <V> type of value
 */
public class DecoratedValue<D, V> {
    private final D decoration;
    private final V value;

    public DecoratedValue(D decoration, V value) {
        this.decoration = decoration;
        this.value = value;
    }

    public D getDecoration() {
        return decoration;
    }

    public V getValue() {
        return value;
    }
}
