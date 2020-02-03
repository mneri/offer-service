package me.mneri.offer.specification;

import lombok.NoArgsConstructor;
import lombok.val;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

/**
 * Utility class for {@code OfferSpecificationIntegrationTest$}* classes.
 *
 * @author mneri
 */
@NoArgsConstructor(access = PRIVATE)
final class OfferSpecificationTestUtil {
    /**
     * Create an {@link Offer} for testing.
     *
     * @param ttl       The time to live for the offer.
     * @param publisher The publisher.
     * @return A new offer with the specified end time and publisher.
     */
    private static Offer createTestOffer(long ttl, User publisher) {
        return Offer.builder()
                .title("Bazinga")
                .description("Awesome")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .ttl(ttl)
                .publisher(publisher)
                .build();
    }

    /**
     * Create an already-expired {@link Offer} for testing.
     *
     * @param publisher The publisher.
     * @return A new already-expired offer.
     */
    static Offer createExpiredTestOffer(User publisher) {
        return createTestOffer(0, publisher);
    }

    /**
     * Create a non-expired {@link Offer} for testing.
     *
     * @param publisher The publisher.
     * @return A new non-expired offer.
     */
    static Offer createNonExpiredTestOffer(User publisher) {
        val ttl = 30 * 24 * 60 * 60 * 1000L;
        return createTestOffer(ttl, publisher);
    }
}
