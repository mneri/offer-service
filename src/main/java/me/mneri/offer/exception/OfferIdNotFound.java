package me.mneri.offer.exception;

public class OfferIdNotFound extends RuntimeException {
    private String id;

    public OfferIdNotFound(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Offer '%s' was not found.", id);
    }
}
