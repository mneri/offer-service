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

import me.mneri.offer.entity.Offer;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification definition for the {@link Offer} entity.
 *
 * @author mneri
 */
public interface OfferSpec {
    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.id = 'value'}.
     *
     * @param value The offer id.
     * @return The specification.
     */
    Specification<Offer> idIsEqualTo(String value);

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.canceled = 1}.
     *
     * @return The specification.
     */
    Specification<Offer> isCanceled();

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.end_time <= NOW()}.
     *
     * @return The specification.
     */
    Specification<Offer> isExpired();

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.canceled = 0 AND offer.end_time >= NOW()}. This
     * is equivalent to the call {@code not(isCanceled()).and(not(isExpired())}.
     *
     * @return The specification.
     */
    Specification<Offer> isOpen();

    /**
     * Return a {@link Specification} for the SQL predicate {@code offer.publisher = 'value'}.
     *
     * @param value The publisher's id.
     * @return The specification.
     */
    Specification<Offer> publisherIdIsEqualTo(String value);

    /**
     * Return a {@link Specification} for the SQL predicate {@code publisher.username = 'value'}.
     *
     * @param value The publisher's username
     * @return The specification.
     */
    Specification<Offer> publisherUsernameIsEqualTo(String value);
}
