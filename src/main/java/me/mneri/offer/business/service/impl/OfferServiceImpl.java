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

package me.mneri.offer.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.mneri.offer.business.bean.OfferCreate;
import me.mneri.offer.business.bean.OfferUpdate;
import me.mneri.offer.business.bean.Paging;
import me.mneri.offer.business.exception.OfferIsCancelledException;
import me.mneri.offer.business.exception.OfferIsExpiredException;
import me.mneri.offer.business.exception.OfferNotFoundException;
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotAuthorizedException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.business.mapping.BusinessLayerMapper;
import me.mneri.offer.business.service.OfferService;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.repository.OfferRepository;
import me.mneri.offer.data.repository.UserRepository;
import me.mneri.offer.data.specification.OfferSpec;
import me.mneri.offer.data.specification.UserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Service for accessing the offer repository.
 *
 * @author Massimo Neri
 */
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class OfferServiceImpl implements OfferService {
    private final BusinessLayerMapper businessLayerMapper;

    private final Clock clock;

    private final OfferRepository offerRepository;

    private final OfferSpec offerSpec;

    private final UserRepository userRepository;

    private final UserSpec userSpec;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(String offerId, String userId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotAuthorizedException, UserNotFoundException {
        User user = userRepository
                .findOne(where(userSpec.idIsEqualTo(userId)))
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        Offer offer = offerRepository
                .findOne(where(offerSpec.idIsEqualTo(offerId)))
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        if (!user.equals(offer.getPublisher())) {
            throw new UserNotAuthorizedException(userId);
        }

        if (offer.isCancelled()) {
            throw new OfferIsCancelledException(offerId);
        }

        if (offer.isExpired(clock)) {
            throw new OfferIsExpiredException(offerId);
        }

        offer.setCancelled(true);

        offerRepository.save(offer);
        log.debug("Offer cancelled; offerId: {}", offerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Offer> findAllOpen(Paging paging) {
        return offerRepository.findAll(where(offerSpec.isOpen()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Offer> findAllOpenByPublisherId(String userId, Paging paging)
            throws UserIsNotEnabledException, UserNotFoundException {
        User user = userRepository
                .findOne(where(userSpec.idIsEqualTo(userId)))
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        return offerRepository.findAll(where(offerSpec.isOpen()).and(offerSpec.publisherIdIsEqualTo(userId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Optional<Offer> findById(String id) {
        return offerRepository.findOne(where(offerSpec.idIsEqualTo(id)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void update(String offerId, OfferUpdate update, String userId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotFoundException, UserNotAuthorizedException {
        User user = userRepository
                .findOne(where(userSpec.idIsEqualTo(userId)))
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        Offer offer = offerRepository
                .findOne(where(offerSpec.idIsEqualTo(offerId)))
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        if (!user.equals(offer.getPublisher())) {
            throw new UserNotAuthorizedException(userId);
        }

        if (offer.isCancelled()) {
            throw new OfferIsCancelledException(offerId);
        }

        if (offer.isExpired(clock)) {
            throw new OfferIsExpiredException(offerId);
        }

        businessLayerMapper.mergeOfferUpdateToOffer(offer, update);

        offerRepository.save(offer);
        log.debug("Offer updated; offerId: {}; userId: {}", offer.getId(), userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Offer save(OfferCreate create, String userId) throws UserIsNotEnabledException, UserNotFoundException {
        User user = userRepository
                .findOne(where(userSpec.idIsEqualTo(userId)))
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        Offer offer = businessLayerMapper.mapOfferCreateToOffer(create, user);

        offerRepository.save(offer);
        log.debug("Offer created; offerId: {}", offer.getId());

        return offer;
    }
}
