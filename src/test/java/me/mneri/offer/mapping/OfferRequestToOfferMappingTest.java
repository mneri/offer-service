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

package me.mneri.offer.mapping;

import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.dto.OfferCreateDto;
import me.mneri.offer.dto.OfferUpdateDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link OfferRequest} to {@link Offer} mapping.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
class OfferRequestToOfferMappingTest {
    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Test the correct initialization of all the fields after the mapping.
     */
    @Test
    void givenOfferCreateDto_whenOfferCreateDtoIsMappedToOffer_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val request = TestUtil.createOfferCreateDto();
        val offer = TestUtil.createNonExpiredOffer(new User("user", "password", passwordEncoder));

        // When
        offerMapper.mergeCreateDtoToEntity(offer, request);

        // Then
        assertEquals(request.getTitle(), offer.getTitle());
        assertEquals(request.getDescription(), offer.getDescription());
        assertEquals(request.getPrice(), offer.getPrice());
        assertEquals(request.getCurrency(), offer.getCurrency());
        assertEquals(request.getTtl(), offer.getTtl());
    }

    /**
     * Test the correct initialization of all the fields after the mapping.
     */
    @Test
    void givenOfferUpdateDto_whenOfferUpdateDtoIsMappedToOffer_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val request = TestUtil.createOfferCreateDto();
        val offer = TestUtil.createNonExpiredOffer(new User("user", "password", passwordEncoder));

        // When
        offerMapper.mergeCreateDtoToEntity(offer, request);

        // Then
        assertEquals(request.getTitle(), offer.getTitle());
        assertEquals(request.getDescription(), offer.getDescription());
        assertEquals(request.getPrice(), offer.getPrice());
        assertEquals(request.getCurrency(), offer.getCurrency());
        assertEquals(request.getTtl(), offer.getTtl());
    }
}
