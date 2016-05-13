package ee.shy.io;

/**
 * Interface for classes wanting to validate their state.
 * Implementations of this interface are invoked within JSON serialization/deserialization via {@link ValidatedTypeAdapterFactory}.
 */
public interface Validated {
    /**
     * Asserts that the object has valid state and throws if it isn't.
     * Should also be called at the end of every constructor.
     * @throws NullPointerException
     * @throws IllegalStateException
     */
    void assertValid();
}
