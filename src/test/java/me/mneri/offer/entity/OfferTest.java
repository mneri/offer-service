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

package me.mneri.offer.entity;

import lombok.val;
import me.mneri.offer.util.TextUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link Offer} class.
 *
 * @author mneri
 */
class OfferTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the creation of a new {@link Offer} instance.
     */
    @Test
    void givenAllParameters_whenNewInstanceIsConstructed_thenAllFieldsAreProperlyInitialized() {
        // Given
        val title = "Bazinga";
        val description = "Awesome";
        val price = new BigDecimal("100.00");
        val currency = "GBP";
        val ttl = 30 * 24 * 60 * 60 * 1000L;
        val publisher = new User("user", "secret", passwordEncoder);

        // When
        val instance = Offer.builder()
                .title(title)
                .description(description)
                .price(price)
                .currency(currency)
                .ttl(ttl)
                .publisher(publisher)
                .build();

        // Then
        assertFalse(TextUtil.isEmpty(instance.getId()));
        assertEquals(title, instance.getTitle());
        assertEquals(description, instance.getDescription());
        assertEquals(0, price.compareTo(instance.getPrice()));
        assertEquals(currency, instance.getCurrency());
        assertEquals(ttl, instance.getTtl());
        assertFalse(instance.isCanceled());
        assertEquals(publisher, instance.getPublisher());
    }
}
