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

package me.mneri.offer.business.exception;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OfferNotFoundExceptionTest {
    @Test
    public void givenOfferId_whenOfferNotFoundExceptionIsCreated_thenOfferIdFieldIsCorrectlyInitialised() {
        // Given
        val offerId = UUID.randomUUID().toString();

        // When
        val exception = new OfferNotFoundException(offerId);

        // Then
        Assertions.assertEquals(offerId, exception.getOfferId());
    }
}
