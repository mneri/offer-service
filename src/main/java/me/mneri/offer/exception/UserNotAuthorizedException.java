package me.mneri.offer.exception;

/**
 * Thrown when a user is not authorized to read or write the specified resource.
 *
 * @author mneri
 */
public class UserNotAuthorizedException extends Exception {
    private String userId;

    /**
     * Create a new instance.
     *
     * @param userId The user id.
     */
    public UserNotAuthorizedException(String userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return String.format("The user identified by '%s' is not authorized.", userId);
    }
}
