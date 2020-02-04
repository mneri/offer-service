package me.mneri.offer.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.Offer_;
import me.mneri.offer.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

import static org.springframework.data.jpa.domain.Specification.not;

/**
 * Utility class for {@link Offer} specification definitions.
 *
 * @author mneri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OfferSpecification {
    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.id = 'value'}.
     *
     * @param value The offer id.
     * @return The specification.
     */
    public static Specification<Offer> offerIdIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(Offer_.id), value);
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.canceled = 1}.
     *
     * @return The specification.
     */
    public static Specification<Offer> offerIsCanceled() {
        return (root, query, builder) -> builder.equal(root.get(Offer_.canceled), true);
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.end_time <= NOW()}.
     *
     * @return The specification.
     */
    public static Specification<Offer> offerIsExpired() {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(Offer_.endTime), new Date());
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.canceled = 0 AND offer.end_time >= NOW()}. This
     * is equivalent to the call {@code not(isCanceled()).and(not(isExpired())}.
     *
     * @return The specification.
     */
    public static Specification<Offer> offerIsOpen() {
        return not(offerIsCanceled()).and(not(offerIsExpired()));
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.publisher = 'value'}.
     *
     * @param value The publisher's id.
     * @return The specification.
     */
    public static Specification<Offer> offerPublisherIdIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.join(Offer_.publisher).get(User_.id), value);
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code publisher.username = 'value'}.
     *
     * @param value The publisher's username
     * @return The specification.
     */
    public static Specification<Offer> offerPublisherUsernameIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.join(Offer_.publisher).get(User_.username), value);
    }
}
