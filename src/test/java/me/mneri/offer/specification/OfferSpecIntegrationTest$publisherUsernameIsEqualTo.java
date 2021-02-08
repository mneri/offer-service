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

import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link OfferSpec#publisherUsernameIsEqualTo(String)} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing an offer published by the specified user;</li>
 *     <li>Repository containing an offer published by another user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OfferSpecIntegrationTest$publisherUsernameIsEqualTo {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferSpec offerSpec;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the SQL predicate {@code user.username = 'value'} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$isCanceledIsCalled_thenNoOfferIsReturn() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);

        // When
        val returned = offerRepository.findAll(
                where(offerSpec.publisherUsernameIsEqualTo(publisher.getUsername())));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.username = 'value'} against a repository containing an offer published by a
     * different user.
     */
    @Test
    void givenOfferPublishedByAnotherUser_whenFindAll$publisherIdIsEqualToIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(other);

        userRepository.save(other);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(
                where(offerSpec.publisherUsernameIsEqualTo(publisher.getUsername())));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.username = 'value'} against a repository containing an offer published by the same
     * user.
     */
    @Test
    void givenOfferPublishedByUser_whenFindAll$publisherIdIsEqualToIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(
                offerSpec.publisherUsernameIsEqualTo(publisher.getUsername())));

        // Then
        assertEquals(1, returned.size());
        assertEquals(offer, returned.get(0));
    }
}
