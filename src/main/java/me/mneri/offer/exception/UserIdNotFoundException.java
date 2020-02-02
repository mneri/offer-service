package me.mneri.offer.exception;

/**
 * Thrown when the specified user id couldn't be found.
 *
 * @author mneri
 */
public class UserIdNotFoundException extends Exception {
    private String userId;

    /**
     * Create a new instance.
     *
     * @param userId The user id.
     */
    public UserIdNotFoundException(String userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return String.format("Couldn't find user id '%s'", userId);
    }
}
