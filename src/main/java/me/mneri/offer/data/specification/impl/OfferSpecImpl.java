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

package me.mneri.offer.data.specification.impl;

import lombok.RequiredArgsConstructor;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.Offer_;
import me.mneri.offer.data.entity.User_;
import me.mneri.offer.data.specification.OfferSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;

import static org.springframework.data.jpa.domain.Specification.not;

/**
 * Default implementation of the {@link OfferSpec} component.
 *
 * @author Massimo Neri
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OfferSpecImpl implements OfferSpec {
    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<Offer> idIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(Offer_.id), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<Offer> isCanceled() {
        return (root, query, builder) -> builder.equal(root.get(Offer_.cancelled), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<Offer> isExpired() {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(Offer_.endTime), new Date(clock.millis()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<Offer> isOpen() {
        return not(isCanceled()).and(not(isExpired()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<Offer> publisherIdIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.join(Offer_.publisher).get(User_.id), value);
    }
}
