package ee.shy.storage;

/**
 * Exception for data integrity being broken.
 * Used when requested (expected) hash of data does nor match data's actual hash.
 */
public class DataIntegrityException extends RuntimeException {
    /**
     * Hash of expected (requested) data (known a priori).
     */
    private final Hash expected;

    /**
     * Hash of actual data (computed).
     */
    private final Hash actual;

    /**
     * Constructs a new data integrity exception with pair of hashes.
     * @param expected hash of expected data
     * @param actual hash of actual data
     */
    public DataIntegrityException(Hash expected, Hash actual) {
        super("expected: " + expected + "; actual: " + actual);

        if (actual.equals(expected))
            throw new IllegalArgumentException("integrity exception with matching hashes");

        this.expected = expected;
        this.actual = actual;
    }

    public Hash getExpected() {
        return expected;
    }

    public Hash getActual() {
        return actual;
    }
}
