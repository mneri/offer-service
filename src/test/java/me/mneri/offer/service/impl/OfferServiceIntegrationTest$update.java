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
import me.mneri.offer.bean.OfferUpdate;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserNotAuthorizedException;
import me.mneri.offer.exception.UserNotFoundException;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the {@link OfferService#update(String, OfferUpdate, String)}.
 * <p>
 * We test 3 different cases:
 * <ul>
 *     <li>The user is not the publisher of the offer;</li>
 *     <li>The offer id is not in the repository;</li>
 *     <li>The user is the publisher and the offer id is in the repository.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class OfferServiceIntegrationTest$update {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test {@link OfferService#update(String, OfferUpdate, String)} given a user id that is different from the id of the offer's
     * publisher.
     */
    @Test
    void givenWrongUserId_whenUpdateIsInvoked_thenOfferUserNotAuthorizedExceptionIsThrown() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);
        val update = TestUtil.createOfferUpdate();

        userRepository.save(publisher);
        userRepository.save(other);
        offerRepository.save(offer);

        // When/Then
        assertThrows(UserNotAuthorizedException.class, () -> offerService.update(offer.getId(), update, other.getId()));
    }

    /**
     * Test {@link OfferService#update(String, OfferUpdate, String)} given a user id that is not present in the repository.
     */
    @Test
    void givenNonExistingUserId_whenUpdateIsInvoked_thenUserIdNotFoundExceptionIsThrown() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val otherId = UUID.randomUUID().toString();
        val offer = TestUtil.createNonExpiredOffer(publisher);
        val update = TestUtil.createOfferUpdate();

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When/Then
        assertThrows(UserNotFoundException.class, () -> offerService.update(offer.getId(), update, otherId));
    }

    /**
     * Test {@link OfferService#update(String, OfferUpdate, String)} given the proper user id.
     */
    @SneakyThrows
    @Test
    void givenUserId_whenUpdateIsInvoked_thenOfferIsUpdated() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);
        val update = TestUtil.createOfferUpdate();

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        offerService.update(offer.getId(), update, publisher.getId());

        // Then
        val optional = offerRepository.findById(offer.getId());

        assertTrue(optional.isPresent());

        val returned = optional.get();

        assertEquals(update.getTitle(), returned.getTitle());
        assertEquals(update.getDescription(), returned.getDescription());
        assertEquals(update.getPrice(), returned.getPrice());
        assertEquals(update.getCurrency(), returned.getCurrency());
    }
}
