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

package me.mneri.offer.business.mapping;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.mneri.offer.business.bean.OfferCreate;
import me.mneri.offer.business.bean.OfferUpdate;
import me.mneri.offer.business.bean.UserCreate;
import me.mneri.offer.data.entity.EntityFactory;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * Helper for the business layer classes mapping.
 *
 * @author Massimo Neri
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class BusinessLayerMapperHelper {
    private final Clock clock;

    private final EntityFactory entityFactory;

    private final PasswordEncoder passwordEncoder;

    /**
     * Return a new {@link Offer} instance.
     *
     * @return A new offer.
     */
    public Offer createOffer() {
        return entityFactory.createOffer();
    }

    /**
     * Return a new {@link User} instance.
     *
     * @return A new user.
     */
    public User createUser() {
        return entityFactory.createUser();
    }

    /**
     * Called after a {@link OfferCreate} to {@link Offer} mapping.
     *
     * @param offer     The offer.
     * @param create    The offer create bean.
     * @param publisher The publisher of the offer.
     */
    @AfterMapping
    public void afterMapOfferCreateToOffer(@MappingTarget @NonNull Offer offer,
                                           OfferCreate create,
                                           @Context @NonNull User publisher) {
        offer.setPublisher(publisher);

        if (create != null) {
            offer.setTtl(create.getTtl(), clock);
        }
    }

    /**
     * Called after a {@link UserCreate} to {@link User} mapping.
     *
     * @param user   The user.
     * @param create The user create bean.
     */
    @AfterMapping
    public void afterMapUserCreateToUser(@MappingTarget @NonNull User user, UserCreate create) {
        if (create != null) {
            user.setEncodedPassword(create.getPassword(), passwordEncoder);
        }
    }

    /**
     * Called after a {@link OfferUpdate} to {@link Offer} mapping.
     *
     * @param offer  The offer.
     * @param update The offer update bean.
     */
    @AfterMapping
    public void afterMergeOfferUpdateToOffer(@MappingTarget Offer offer, OfferUpdate update) {
        if (update.getTtl() != null) {
            offer.setTtl(update.getTtl(), clock);
        }
    }
}
