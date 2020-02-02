package me.mneri.offer.specification;

import lombok.NoArgsConstructor;
import me.mneri.offer.entity.User;
import org.springframework.data.jpa.domain.Specification;

import static lombok.AccessLevel.PRIVATE;
import static me.mneri.offer.entity.User_.*;

/**
 * Utility class for {@link User} specification definitions.
 *
 * @author mneri
 */
@NoArgsConstructor(access = PRIVATE)
public final class UserSpecification {
    /**
     * Return a {@link Specification} for the SQL predicate {@code user.id = 'value'}.
     *
     * @param value The value to match against the id.
     * @return The specification.
     */
    public static Specification<User> userIdIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(id), value);
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code user.enabled = 1}.
     *
     * @return The specification.
     */
    public static Specification<User> userIsEnabled() {
        return (root, query, builder) -> builder.equal(root.get(enabled), true);
    }

    /**
     * Return a {@link Specification} for the SQL predicate {@code user.username = 'value'}.
     *
     * @param value The value to match against username.
     * @return The specification.
     */
    public static Specification<User> userUsernameIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(username), value);
    }
}
