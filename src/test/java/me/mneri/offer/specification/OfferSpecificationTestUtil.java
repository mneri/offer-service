package me.mneri.offer.specification;

import lombok.NoArgsConstructor;
import lombok.val;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;

import java.math.BigDecimal;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class OfferSpecificationTestUtil {
    /**
     * Create an {@link Offer} for testing.
     *
     * @param end       The end time for the offer.
     * @param publisher The publisher.
     * @return A new offer with the specified end time and publisher.
     */
    private static Offer createTestOffer(Date end, User publisher) {
        return Offer.builder()
                .title("Bazinga")
                .description("Awesome")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .end(end)
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
        // The offer ended yesterday.
        val end = new Date(currentTimeMillis() - 24 * 60 * 60 * 1000L);
        return createTestOffer(end, publisher);
    }

    /**
     * Create a non-expired {@link Offer} for testing.
     *
     * @param publisher The publisher.
     * @return A new non-expired offer.
     */
    static Offer createNonExpiredTestOffer(User publisher) {
        // The offer started 31 days ago and ended yesterday
        val end = new Date(currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L);
        return createTestOffer(end, publisher);
    }
}
