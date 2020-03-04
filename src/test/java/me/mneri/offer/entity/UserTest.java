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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link User} class.
 *
 * @author mneri
 */
class UserTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test if the fields are correctly initialized upon construction.
     */
    @Test
    void givenUsernameRawPasswordAndEncoder_whenConstructorIsInvoked_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val username = "user";
        val rawPassword = "pass";

        // When
        val user = new User(username, rawPassword, passwordEncoder);

        // Then
        assertNotNull(user.getId());
        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches(rawPassword, user.getEncodedPassword()));
        assertTrue(user.isEnabled());
    }

    /**
     * Test if the password is set correctly.
     */
    @Test
    void givenRawPasswordAndEncoder_whenSetEncodedPasswordIsInvoked_thenPasswordIsCorrectlySet() {
        // Given
        val rawPassword = "secret";
        val user = new User("user", "", passwordEncoder);

        // When
        user.setEncodedPassword(rawPassword, passwordEncoder);

        // Then
        assertTrue(passwordEncoder.matches(rawPassword, user.getEncodedPassword()));
    }
}
