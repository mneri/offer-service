package me.mneri.offer.service.impl;

import lombok.NoArgsConstructor;
import lombok.val;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * Utility class for {@code OfferServiceIntegrationTest$}* classes.
 *
 * @author mneri
 */
@NoArgsConstructor(access = PRIVATE)
final class OfferServiceTestUtil {
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
     * Create a list of closed {@link Offer}s for testing.
     * <p>
     * The list includes all the possible combinations of expiration and cancellation.
     *
     * @param publisher The publisher.
     * @return A list of closed offers.
     */
    static List<Offer> createClosedTestOffers(User publisher) {
        val closed1 = createExpiredTestOffer(publisher);

        val closed2 = createExpiredTestOffer(publisher);
        closed2.setCanceled(true);

        val closed3 = createNonExpiredTestOffer(publisher);
        closed3.setCanceled(true);

        return Arrays.asList(closed1, closed2, closed3);
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
