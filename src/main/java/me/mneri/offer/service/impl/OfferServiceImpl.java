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
import me.mneri.offer.exception.OfferIdNotFoundException;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;
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
    public void delete(Offer offer, String userId) throws UserIdNotFoundException, UserNotAuthorizedException {
        if (!enabledUserExistsById(userId)) {
            log.debug("No enabled user with the specified id was found; userId: {}", userId);
            throw new UserIdNotFoundException(userId);
        }

        if (offerRepository.count(where(offerSpec.idIsEqualTo(offer.getId())).and(offerSpec.publisherIdIsEqualTo(userId))) == 0) {
            throw new UserNotAuthorizedException(userId);
        }

        offer.setCanceled(true);
        offerRepository.save(offer);
    }

    /**
     * Return {@code true} if an enabled {@link User} with the specified id exists in the repository.
     *
     * @param userId The user id.
     * @return {@code true} if such a user exists, {@code false} otherwise.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean enabledUserExistsById(String userId) {
        return userRepository.count(where(userSpec.isEnabled()).and(userSpec.idIsEqualTo(userId))) > 0;
    }

    /**
     * {@inheritDoc}
     */
    public List<Offer> findAllOpen() {
        return offerRepository.findAll(where(offerSpec.isOpen()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Offer> findAllOpenByPublisherId(String userId) throws UserIdNotFoundException {
        if (!enabledUserExistsById(userId)) {
            log.debug("No enabled user with the specified id was found; userId: {}", userId);
            throw new UserIdNotFoundException(userId);
        }

        return offerRepository.findAll(where(offerSpec.isOpen()).and(offerSpec.publisherIdIsEqualTo(userId)));
    }

    /**
     * {@inheritDoc}
     */
    public List<Offer> findAllOpenByPublisherUsername(String username) {
        return offerRepository.findAll(where(offerSpec.isOpen()).and(offerSpec.publisherUsernameIsEqualTo(username)));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Offer> findOpenById(String id) {
        return offerRepository.findOne(where(offerSpec.isOpen()).and(offerSpec.idIsEqualTo(id)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void update(String offerId, OfferUpdate update, String userId)
            throws OfferIdNotFoundException, UserIdNotFoundException, UserNotAuthorizedException {
        if (!enabledUserExistsById(userId)) {
            log.debug("No enabled user with the specified id was found; userId: {}", userId);
            throw new UserIdNotFoundException(userId);
        }

        Optional<Offer> optional = offerRepository.findOne(where(offerSpec.isOpen()).and(offerSpec.idIsEqualTo(offerId)));

        if (!optional.isPresent()) {
            log.debug("No open offer with the specified id was found; offerId: {}", offerId);
            throw new OfferIdNotFoundException(offerId);
        }

        Offer offer = optional.get();
        offerMapper.mergeUpdateToEntity(offer, update);

        if (offerRepository.count(where(offerSpec.idIsEqualTo(offer.getId())).and(offerSpec.publisherIdIsEqualTo(userId))) == 0) {
            log.debug("The user is not authorized to update the offer; offerId: {}; userId: {}", offer.getId(), userId);
            throw new UserNotAuthorizedException(userId);
        }

        offerRepository.save(offer);
        log.debug("Offer updated; offerId: {}; userId: {}", offer.getId(), userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(OfferCreate create, User user) {
        Offer offer = Offer.builder().build();
        offerMapper.mergeCreateToOffer(offer, create);
        offer.setPublisher(user);
        offerRepository.save(offer);
        log.debug("Offer created; offerId: {}", offer.getId());
    }
}
