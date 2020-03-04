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

import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the {@link OfferService#findAllOpenByPublisherId(String)} method.<br/>
 * We test 5 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing only closed offers by the specified user;</li>
 *     <li>Repository containing only closed offers by another user;</li>
 *     <li>Repository containing a single open offer by the specified user;</li>
 *     <li>Repository containing a single open offer by another user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OfferServiceIntegrationTest$findAllOpenByPublisherId {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherId(String)} against an repository containing only
     * closed offers published by the specified user.
     */
    @SneakyThrows
    @Test
    void givenClosedOffersPublishedByUser_whenFindAllOpenByPublisherIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offers = TestUtil.createClosedOfferList(publisher);

        userRepository.save(publisher);

        for (val offer : offers) {
            offerRepository.save(offer);
        }

        // When
        val returned = offerService.findAllOpenByPublisherId(publisher.getId());

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherId(String)} against an repository containing only
     * closed offers published by another user.
     */
    @SneakyThrows
    @Test
    void givenClosedOffersPublishedByAnotherUser_whenFindAllOpenByPublisherIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offers = TestUtil.createClosedOfferList(other);

        userRepository.save(other);

        for (val offer : offers) {
            offerRepository.save(offer);
        }

        // When/Then
        assertThrows(UserIdNotFoundException.class, () -> offerService.findAllOpenByPublisherId(publisher.getId()));
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherId(String)} against an empty repository.
     */
    @SneakyThrows
    @Test
    void givenEmptyRepository_whenFindAllOpenByPublisherIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);

        // When/Then
        assertThrows(UserIdNotFoundException.class, () -> offerService.findAllOpenByPublisherId(publisher.getId()));
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherId(String)} against an repository containing a single
     * open offers published by another user.
     */
    @SneakyThrows
    @Test
    void givenOpenOfferPublishedByAnotherUser_whenFindAllOpenByPublisherIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(other);

        userRepository.save(other);
        offerRepository.save(offer);

        // When/Then
        assertThrows(UserIdNotFoundException.class, () -> offerService.findAllOpenByPublisherId(publisher.getId()));
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherId(String)} against an repository containing a single
     * open offers published by the specified user.
     */
    @SneakyThrows
    @Test
    void givenOpenOfferPublishedByUser_whenFindAllOpenByPublisherIdIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerService.findAllOpenByPublisherId(publisher.getId());

        // Then
        assertEquals(1, returned.size());
        assertTrue(returned.contains(offer));
    }
}
