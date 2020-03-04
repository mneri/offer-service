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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static me.mneri.offer.specification.OfferSpecification.offerIdIsEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link OfferSpecification#offerIdIsEqualTo(String)} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing an offer with the specified id;</li>
 *     <li>Repository containing an offer with a different id.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
class OfferSpecificationIntegrationTest$idIsEqualTo {
    @Autowired
    private OfferRepository offerRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the SQL predicate {@code offer.id = 'vaule'} against a repository containing an offer with the specified id.
     */
    @Test
    void givenOffer_whenFindAll$idIsEqualToIdCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(offerIdIsEqualTo(offer.getId())));

        // Then
        assertEquals(1, returned.size());
        assertEquals(offer, returned.get(0));
    }

    /**
     * Test the SQL predicate {@code offer.id = 'value'} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$isCanceledIsCalled_thenNoOfferIsReturn() {
        // Given
        val id = UUID.randomUUID().toString();

        // When
        val returned = offerRepository.findAll(where(offerIdIsEqualTo(id)));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code offer.id = 'value'} against a repository containing an offer with a different id.
     */
    @Test
    void givenOfferWithDifferentId_whenFindAll$idIsEqualToIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(offerIdIsEqualTo(UUID.randomUUID().toString())));

        // Then
        assertTrue(returned.isEmpty());
    }
}
