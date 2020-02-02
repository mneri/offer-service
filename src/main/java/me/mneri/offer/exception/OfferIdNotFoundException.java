package me.mneri.offer.exception;

/**
 * Thrown when the specified offer id couldn't be found.
 *
 * @author mneri
 */
public class OfferIdNotFoundException extends Throwable {
    private String offerId;

    /**
     * Create a new instance.
     *
     * @param offerId The offer id.
     */
    public OfferIdNotFoundException(String offerId) {
        this.offerId = offerId;
    }

    @Override
    public String getMessage() {
        return String.format("Couldn't find the offer id '%s'", offerId);
    }
}
