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

package me.mneri.offer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.mneri.offer.dto.OfferCreateDto;
import me.mneri.offer.dto.OfferUpdateDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for {@code OfferServiceIntegrationTest$}* classes.
 *
 * @author mneri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtil {
    /**
     * Create a list of closed {@link Offer}s for testing.
     * <p>
     * The list includes all the possible combinations of expiration and cancellation.
     *
     * @param publisher The publisher.
     * @return A list of closed offers.
     */
    public static List<Offer> createClosedOfferList(User publisher) {
        val closed1 = createExpiredOffer(publisher);

        val closed2 = createExpiredOffer(publisher);
        closed2.setCanceled(true);

        val closed3 = createNonExpiredOffer(publisher);
        closed3.setCanceled(true);

        return Arrays.asList(closed1, closed2, closed3);
    }

    /**
     * Create an already-expired {@link Offer} for testing.
     *
     * @param publisher The publisher.
     * @return A new already-expired offer.
     */
    public static Offer createExpiredOffer(User publisher) {
        return createOffer(0, publisher);
    }

    /**
     * Create a non-expired {@link Offer} for testing.
     *
     * @param publisher The publisher.
     * @return A new non-expired offer.
     */
    public static Offer createNonExpiredOffer(User publisher) {
        val ttl = 30 * 24 * 60 * 60 * 1000L;
        return createOffer(ttl, publisher);
    }

    /**
     * Create an {@link Offer} for testing.
     *
     * @param ttl       The time to live for the offer.
     * @param publisher The publisher.
     * @return A new offer with the specified end time and publisher.
     */
    private static Offer createOffer(long ttl, User publisher) {
        return Offer.builder()
                .title("Bazinga")
                .description("Awesome")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .ttl(ttl)
                .publisher(publisher)
                .build();
    }

    /**
     * Create a new test {@link OfferCreateDto}.
     *
     * @return The {@code OfferCreateDto}.
     */
    public static OfferCreateDto createOfferCreateDto() {
        OfferCreateDto offerCreateDto = new OfferCreateDto();
        offerCreateDto.setTitle("New Title");
        offerCreateDto.setDescription("New Description");
        offerCreateDto.setPrice(new BigDecimal("999.99"));
        offerCreateDto.setCurrency("EUR");
        offerCreateDto.setTtl(10000L);
        return offerCreateDto;
    }

    /**
     * Create a new test {@link OfferUpdateDto}.
     *
     * @return The {@code OfferRequest}.
     */
    public static OfferUpdateDto createOfferUpdateDto() {
        OfferUpdateDto offerCreateDto = new OfferUpdateDto();
        offerCreateDto.setTitle("New Title");
        offerCreateDto.setDescription("New Description");
        offerCreateDto.setPrice(new BigDecimal("999.99"));
        offerCreateDto.setCurrency("EUR");
        offerCreateDto.setTtl(10000L);
        return offerCreateDto;
    }
}
