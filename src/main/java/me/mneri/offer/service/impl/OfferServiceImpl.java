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

package me.mneri.offer.service.impl;

import lombok.extern.log4j.Log4j2;
import me.mneri.offer.bean.OfferCreate;
import me.mneri.offer.bean.OfferUpdate;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.*;
import me.mneri.offer.mapping.OfferMapper;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.specification.OfferSpec;
import me.mneri.offer.specification.UserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Service for accessing the offer repository.
 *
 * @author mneri
 */
@Log4j2
@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferSpec offerSpec;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSpec userSpec;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(String offerId, String userId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotAuthorizedException, UserNotFoundException {
        User user = userRepository.findOne(where(userSpec.idIsEqualTo(userId)))
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        Offer offer = offerRepository.findOne(where(offerSpec.idIsEqualTo(offerId)))
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        if (!offer.getPublisher().equals(user)) {
            throw new UserNotAuthorizedException(userId);
        }

        if (offer.isCanceled()) {
            throw new OfferIsCancelledException(offerId);
        }

        if (offer.getTtl() == 0) {
            throw new OfferIsExpiredException(offerId);
        }

        offer.setCanceled(true);

        offerRepository.save(offer);
        log.debug("Offer cancelled; offerId: {}", offerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Offer> findAllOpen() {
        return offerRepository.findAll(where(offerSpec.isOpen()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Offer> findAllOpenByPublisherId(String userId) throws UserIsNotEnabledException, UserNotFoundException {
        User user = userRepository.findOne(where(userSpec.idIsEqualTo(userId)))
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
        User user = userRepository.findOne(where(userSpec.idIsEqualTo(userId)))
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        Offer offer = offerRepository.findOne(where(offerSpec.idIsEqualTo(offerId)))
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        if (!offer.getPublisher().equals(user)) {
            throw new UserNotAuthorizedException(userId);
        }

        if (offer.isCanceled()) {
            throw new OfferIsCancelledException(offerId);
        }

        if (offer.getTtl() == 0) {
            throw new OfferIsExpiredException(offerId);
        }

        offerMapper.mergeUpdateToEntity(offer, update);

        offerRepository.save(offer);
        log.debug("Offer updated; offerId: {}; userId: {}", offer.getId(), userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Offer save(OfferCreate create, String userId) throws UserIsNotEnabledException, UserNotFoundException {
        User user = userRepository.findOne(where(userSpec.idIsEqualTo(userId)))
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        Offer offer = Offer.builder().build();
        offerMapper.mergeCreateToEntity(offer, create);
        offer.setPublisher(user);

        offerRepository.save(offer);
        log.debug("Offer created; offerId: {}", offer.getId());

        return offer;
    }
}
