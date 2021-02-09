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
import me.mneri.offer.bean.OfferCreate;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the {@link OfferService#save(OfferCreate, String)}.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class OfferServiceIntegrationTest$save {
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
     * Test {@link OfferService#save(OfferCreate, String)} saving a {@link Offer} and then retrieving it from the
     * repository.
     */
    @SneakyThrows
    @Test
    void givenOffer_whenSaveIsInvoked_thenOfferIsRetrievable() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val create = TestUtil.createOfferCreate();

        userRepository.save(publisher);

        // When
        val offer = offerService.save(create, publisher.getId());

        // Then
        val optional = offerRepository.findById(offer.getId());

        assertTrue(optional.isPresent());

        val returned = optional.get();

        assertEquals(offer.getId(), returned.getId());
        assertEquals(offer.getTitle(), returned.getTitle());
        assertEquals(offer.getDescription(), returned.getDescription());
        assertEquals(offer.getPrice(), returned.getPrice());
        assertEquals(offer.getCurrency(), returned.getCurrency());
    }
}
