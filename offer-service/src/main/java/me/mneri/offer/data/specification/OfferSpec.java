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
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.Offer_;
import me.mneri.offer.data.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import java.time.Clock;
import java.util.Date;
import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.not;

/**
 * Default implementation of the {@link OfferSpec} component.
 *
 * @author Massimo Neri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferSpec {
    public static Specification<Offer> idIsEqualTo(UUID value) {
        return (root, query, builder) -> builder.equal(root.get(Offer_.id), value);
    }

    public static Specification<Offer> isCanceled() {
        return (root, query, builder) -> builder.equal(root.get(Offer_.cancelled), true);
    }

    public static Specification<Offer> isExpired(Clock clock) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(Offer_.endTime), new Date(clock.millis()));
    }

    public static Specification<Offer> isOpen(Clock clock) {
        return not(isCanceled()).and(not(isExpired(clock)));
    }

    public static Specification<Offer> publisherIdIsEqualTo(UUID uuid) {
        return (root, query, builder) -> builder.equal(root.join(Offer_.publisher).get(User_.id), uuid);
    }

    public static Specification<Offer> publisherUsernameIsEqualTo(String username) {
        return (root, query, builder) -> builder.equal(root.join(Offer_.publisher).get(User_.username), username);
    }
}
