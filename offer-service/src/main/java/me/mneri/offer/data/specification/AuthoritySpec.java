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

package me.mneri.offer.data.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.mneri.offer.data.entity.Authority;
import me.mneri.offer.data.entity.Authority_;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.UUID;

/**
 * Default implementation of the {@link AuthoritySpec} component.
 *
 * @author Massimo Neri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthoritySpec {
    public static Specification<Authority> isEnabled() {
        return (root, query, builder) -> builder.equal(root.get(Authority_.enabled), true);
    }

    public static Specification<Authority> ownerIdIsEqualTo(UUID userId) {
        return (root, query, builder) -> {
            Subquery<UUID> subQuery = query.subquery(UUID.class);
            Root<User> subRoot = subQuery.from(User.class);
            Join<User, Authority> join = subRoot.join(User_.authorities);

            subQuery.select(join.get(Authority_.id))
                    .where(builder.equal(subRoot.get(User_.id), userId));

            return root.get(Authority_.id).in(subQuery);
        };
    }
}
