package ee.shy;

/**
 * An exception with a category to be presented to the user.
 */
public class UserPresentableException extends RuntimeException {
    /**
     * Category of the exception.
     */
    private final String category;

    public UserPresentableException(String category) {
        super();
        this.category = category;
    }

    public UserPresentableException(String category, String message) {
        super(message);
        this.category = category;
    }

    public UserPresentableException(String category, String message, Throwable cause) {
        super(message, cause);
        this.category = category;
    }

    public UserPresentableException(String category, Throwable cause) {
        super(cause);
        this.category = category;
    }

    protected UserPresentableException(String category, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String getLocalizedMessage() {
        return getCategory() + ": " + getMessage();
    }
}
