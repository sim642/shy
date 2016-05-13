package ee.shy;

/**
 * Interface that a builder class should implement.
 */
public interface Builder<T> {
    /**
     * Builds final object.
     * @return object of type {@link T}
     */
    @SuppressWarnings("unused")
    T create();
}
