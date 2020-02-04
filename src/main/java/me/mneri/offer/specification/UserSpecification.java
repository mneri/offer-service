package me.mneri.offer.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.mneri.offer.entity.User;
import me.mneri.offer.entity.User_;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class for {@link User} specification definitions.
 *
 * @author mneri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSpecification {
    /**
     * Return a {@link Specification} for the SQL predicate {@code user.id = 'value'}.
     *
     * @param value The value to match against the id.
     * @return The specification.
     */
    public static Specification<User> userIdIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(User_.id), value);
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code user.enabled = 1}.
     *
     * @return The specification.
     */
    public static Specification<User> userIsEnabled() {
        return (root, query, builder) -> builder.equal(root.get(User_.enabled), true);
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code user.username = 'value'}.
     *
     * @param value The value to match against username.
     * @return The specification.
     */
    public static Specification<User> userUsernameIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(User_.username), value);
    }
}
