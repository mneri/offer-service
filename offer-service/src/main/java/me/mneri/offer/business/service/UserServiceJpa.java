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
import lombok.extern.log4j.Log4j2;
import me.mneri.offer.business.exception.OfferIsCancelledException;
import me.mneri.offer.business.exception.OfferIsExpiredException;
import me.mneri.offer.business.exception.OfferNotFoundException;
import me.mneri.offer.business.mapping.BusinessLayerMapper;
import me.mneri.offer.business.pojo.Paging;
import me.mneri.offer.business.pojo.UserCreate;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.repository.OfferRepository;
import me.mneri.offer.data.repository.UserRepository;
import me.mneri.offer.data.specification.OfferSpec;
import me.mneri.offer.data.specification.UserSpec;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Service for accessing the user repository.
 *
 * @author Massimo Neri
 */
@Log4j2
@Primary
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
class UserServiceJpa implements UserService {
    private final BusinessLayerMapper businessLayerMapper;

    private final Clock clock;

    private final OfferRepository offerRepository;

    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<User> findAllEnabled(Paging paging) {
        return userRepository.findAll(where(UserSpec.isEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Optional<User> findById(UUID userId) {
        return userRepository.findOne(where(UserSpec.idIsEqualTo(userId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User findByOfferId(UUID offerId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException {
        Offer offer = offerRepository
                .findOne(where(OfferSpec.idIsEqualTo(offerId)))
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        if (offer.isCancelled()) {
            throw new OfferIsCancelledException(offerId);
        }

        if (offer.isExpired(clock)) {
            throw new OfferIsExpiredException(offerId);
        }

        return userRepository
                .findOne(where(UserSpec.isPublisherOf(offerId)))
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findOne(where(UserSpec.usernameIsEqualTo(username)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User save(UserCreate create) {
        User user = businessLayerMapper.mapUserCreateToUser(create);

        userRepository.save(user);
        log.debug("User created; userId: {}", user.getId());

        return user;
    }
}
