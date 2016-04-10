package ee.shy;

/**
 * Interface for exceptions presentable to the user.
 */
public interface UserPresentableException {
    /**
     * Prerequisite from {@link Throwable} for default {@link UserPresentableException#getUserMessage()}.
     * @see Throwable#getLocalizedMessage()
     * @return localized description of this exception
     */
    String getLocalizedMessage();

    /**
     * Returns the message to be displayed to the user.
     * Defaults to {@link Throwable#getLocalizedMessage()}.
     * @return message to show the user
     */
    default String getUserMessage() {
        return getLocalizedMessage();
    }
}
