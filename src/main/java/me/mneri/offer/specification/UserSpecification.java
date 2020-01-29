package me.mneri.offer.specification;

import lombok.NoArgsConstructor;
import me.mneri.offer.entity.User;
import org.springframework.data.jpa.domain.Specification;

import static lombok.AccessLevel.PRIVATE;
import static me.mneri.offer.entity.User_.enabled;
import static me.mneri.offer.entity.User_.username;

@NoArgsConstructor(access = PRIVATE)
public final class UserSpecification {
    /**
     * Return a specification for the SQL predicate {@code user.enabled = 1}.
     *
     * @return The specification.
     */
    public static Specification<User> isEnabled() {
        return (root, query, builder) -> builder.equal(root.get(enabled), true);
    }

    /**
     * Return a specification for the SQL predicate {@code user.username = 'value'}.
     *
     * @param value The value to match against username.
     * @return The specification.
     */
    public static Specification<User> usernameIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(username), value);
    }
}
