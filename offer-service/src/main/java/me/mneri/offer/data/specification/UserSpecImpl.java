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

import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.Offer_;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.entity.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.UUID;

/**
 * Default implementation of the {@link UserSpec} component.
 */
@Component
public class UserSpecImpl implements UserSpec {
    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<User> idIsEqualTo(UUID value) {
        return (root, query, builder) -> builder.equal(root.get(User_.id), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<User> isEnabled() {
        return (root, query, builder) -> builder.equal(root.get(User_.enabled), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<User> isPublisherOf(UUID offerId) {
        return (root, query, builder) -> {
            Subquery<UUID> subQuery = query.subquery(UUID.class);
            Root<Offer> subRoot = subQuery.from(Offer.class);

            subQuery.select(subRoot.get(Offer_.publisher).get(User_.id))
                    .where(builder.equal(subRoot.get(Offer_.id), offerId));

            return builder.equal(root.get(User_.id), subQuery);
        };
    }
}
