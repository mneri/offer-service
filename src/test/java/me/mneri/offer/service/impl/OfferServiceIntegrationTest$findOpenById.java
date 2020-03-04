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

import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the {@link OfferService#findOpenById(String)} method. <br/>
 * We test 4 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing an open offer with the same id;</li>
 *     <li>Repository containing a closed offer with the same id;</li>
 *     <li>Repository containing a different offer.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OfferServiceIntegrationTest$findOpenById {
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
     * Test the method {@link UserService#findEnabledById(String)} against a repository containing the specified offer,
     * but it's closed.
     */
    @Test
    void givenClosedOfferInRepository_whenFindOpenByIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerService.findOpenById(offer.getId());

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledById(String)} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindByOpenByIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val id = UUID.randomUUID().toString();

        // When
        val returned = offerService.findOpenById(id);

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledById(String)} against a repository containing the specified offer.
     */
    @Test
    void givenOpenOfferInRepository_whenFindOpenByIdIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerService.findOpenById(offer.getId());

        // Then
        assertTrue(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledById(String)} against a repository containing the specified offer.
     */
    @Test
    void givenOpenOfferWithDifferentIdInRepository_whenFindOpenByIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);
        val offerId = UUID.randomUUID().toString();

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerService.findOpenById(offerId);

        // Then
        assertFalse(returned.isPresent());
    }
}
