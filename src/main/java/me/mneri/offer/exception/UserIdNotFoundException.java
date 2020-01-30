package me.mneri.offer.exception;

public class UserIdNotFoundException extends RuntimeException {
    private String id;

    public UserIdNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("User '%s' was not found.", id);
    }
}
