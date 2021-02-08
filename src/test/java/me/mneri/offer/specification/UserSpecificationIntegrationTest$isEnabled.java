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
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
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
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link UserSpec#isEnabled()} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing an enabled user;</li>
 *     <li>Repository containing a disabled user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserSpecificationIntegrationTest$isEnabled {
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSpec userSpec;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    private User createTestUser() {
        return new User("user", "secret", passwordEncoder);
    }

    /**
     * Test the SQL predicate {@code user.enabled = 1} against a repository containing an enabled user.
     */
    @Test
    void givenEnabledUserInRepository_whenFindAll$isEnabledIsCalled_thenUserIsReturned() {
        // Given
        val user = createTestUser();

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userSpec.isEnabled()));

        // Then
        assertEquals(1, returned.size());
        assertTrue(returned.contains(user));
    }

    /**
     * Test the SQL predicate {@code user.enabled = 1} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$isEnabledIsCalled_thenNoUserIsReturned() {
        // Given
        // Empty repository

        // When
        val returned = userRepository.findAll(where(userSpec.isEnabled()));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.enabled = 1} against a repository containing a single disabled user.
     */
    @Test
    void givenDisabledUserInRepository_whenFindAll$isEnabledIsCalled_thenNoUserIsReturned() {
        // Given
        val user = createTestUser();

        user.setEnabled(false);
        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userSpec.isEnabled()));

        // Then
        assertTrue(returned.isEmpty());
    }
}
