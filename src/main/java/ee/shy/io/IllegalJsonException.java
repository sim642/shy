package ee.shy.io;

/**
 * Exception for JSON being syntactically correct but semantically incorrect,
 * i.e. missing required fields or being provided illegal arguments.
 */
public class IllegalJsonException extends RuntimeException {
    public IllegalJsonException(String message) {
        super(message);
    }

    public IllegalJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalJsonException(Throwable cause) {
        super(cause);
    }
}
