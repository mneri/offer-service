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

package me.mneri.offer.util;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextUtilTest {
    @Test
    void givenEmptyString_whenIsEmptyIsInvoked_thenTrueIsReturned() {
        // When
        boolean empty = TextUtil.isEmpty("");

        // Then
        assertTrue(empty);
    }

    @Test
    void giveNonEmptyString_whenIsEmptyIsInvoked_thenFalseIsReturned() {
        // Given
        val string = "Hello, world!";

        // When
        boolean empty = TextUtil.isEmpty(string);

        // Then
        assertFalse(empty);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void givenNull_whenIsEmptyIsInvoked_thenTrueIsReturned() {
        // When
        boolean empty = TextUtil.isEmpty(null);

        // Then
        assertTrue(empty);
    }
}
