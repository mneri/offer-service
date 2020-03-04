/*
 * Copyright 2020 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mneri/offer-service.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
