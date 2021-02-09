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

import lombok.val;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test the {@link UserService#findById(String)} method. <br/>
 * We test 4 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a user with different id;</li>
 *     <li>Repository containing a disabled user with the same id;</li>
 *     <li>Repository containing an enabled user with the same id;</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceIntegrationTest$findEnabledById {
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the method {@link UserService#findById(String)} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindByUsernameIsCalled_thenNoUserIsReturned() {
        // Given
        val id = UUID.randomUUID().toString();

        // When
        val returned = userService.findById(id);

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findById(String)} against a repository that does not contain the
     * specified id.
     */
    @Test
    void givenUsernameAndDifferentEnabledUserInRepository_whenFindAll$usernameIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userService.findById(other.getId());

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findById(String)} against a repository that contains the specified
     * username and the user is enabled.
     */
    @Test
    void givenUsernameAndEnabledUserInRepository_whenFindByUsernameIsCalled_thenUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userService.findById(user.getId());

        // Then
        assertEquals(user, returned.orElse(null));
    }
}
