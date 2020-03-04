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

package me.mneri.offer.repository;

import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test {@link OfferRepository} against {@link Offer} constraints.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
class OfferRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OfferRepository offerRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Create a test {@link User}.
     *
     * @return A new user.
     */
    private User createTestPublisher() {
        return new User("user", "secret", passwordEncoder);
    }

    /**
     * Test {@link OfferRepository#save(Object)} against an {@link Offer} with blank description.
     */
    @Test
    void givenBlankDescription_whenOfferIsPersisted_thenConstraintViolationExceptionIsThrown() {
        // Given
        val publisher = createTestPublisher();
        val offer = TestUtil.createNonExpiredOffer(publisher);

        testEntityManager.persist(publisher);
        offer.setDescription("");

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            offerRepository.save(offer);
            testEntityManager.flush();
        });
    }

    /**
     * Test {@link OfferRepository#save(Object)} against an {@link Offer} with blank title.
     */
    @Test
    void givenBlankTitle_whenOfferIsPersisted_thenConstraintViolationExceptionIsThrown() {
        // Given
        val publisher = createTestPublisher();
        val offer = TestUtil.createNonExpiredOffer(publisher);

        testEntityManager.persist(publisher);
        offer.setTitle("");

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            offerRepository.save(offer);
            testEntityManager.flush();
        });
    }

    @Test
    void givenLegitOffer_whenOfferIsPersisted_thenNoExceptionIsThrown() {
        // Given
        val publisher = createTestPublisher();
        val offer = TestUtil.createNonExpiredOffer(publisher);

        testEntityManager.persist(publisher);

        // When
        offerRepository.save(offer);
        testEntityManager.flush();

        // Then
        // No exceptions.
    }
}
