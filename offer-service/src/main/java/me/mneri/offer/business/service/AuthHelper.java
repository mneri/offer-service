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

package me.mneri.offer.business.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.repository.OfferRepository;
import me.mneri.offer.data.repository.UserRepository;
import me.mneri.offer.data.specification.OfferSpec;
import me.mneri.offer.data.specification.UserSpec;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Helper class for service authorization.
 *
 * @author Massimo Neri
 */
@Component("authHelper")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
class AuthHelper {
    private final OfferRepository offerRepository;

    private final UserRepository userRepository;

    /**
     * Return {@code true} if the specified {@link Offer} is published by the specified {@link User}.
     *
     * @param offerId  The offer id.
     * @param username The user's username.
     * @return {@code true} if the specified offer is published by the specified user, {@code false} otherwise.
     */
    @Transactional
    public boolean isPublishedByUser(UUID offerId, String username) {
        long count = offerRepository.count(
                where(OfferSpec.idIsEqualTo(offerId)
                        .and(OfferSpec.publisherUsernameIsEqualTo(username))));
        return count == 1;
    }

    /**
     * Return {@code true} if the user identified by the specified id has the specified username.
     *
     * @param userId   The user id.
     * @param username The user name.
     * @return {@code true} if the user exists and has the specified username, {@code false} otherwise.
     */
    @Transactional
    public boolean isUsernameEqualTo(UUID userId, String username) {
        long count = userRepository
                .count(where(UserSpec.idIsEqualTo(userId))
                        .and(UserSpec.usernameIsEqualTo(username)));
        return count == 1;
    }
}
