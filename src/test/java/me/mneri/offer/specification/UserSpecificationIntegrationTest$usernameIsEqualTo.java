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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static me.mneri.offer.specification.UserSpecification.userUsernameIsEqualTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link UserSpecification#userUsernameIsEqualTo(String)} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a user with the specified username;</li>
 *     <li>Repository containing a user with a different username.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserSpecificationIntegrationTest$usernameIsEqualTo {
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    private User createTestUser() {
        return new User("user", "secret", passwordEncoder);
    }

    /**
     * Test the SQL predicate {@code user.username = 'value'} against a repository containing the wanted username.
     */
    @Test
    void givenUsernameAndUserInRepository_whenFindAll$usernameIsEqualToIsCalled_thenUserIsReturned() {
        // Given
        val user = createTestUser();

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userUsernameIsEqualTo(user.getUsername())));

        // Then
        assertTrue(returned.contains(user));
    }

    /**
     * Test the SQL predicate {@code user.username = 'value'} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$usernameIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val username = "user";

        // When
        val returned = userRepository.findAll(where(userUsernameIsEqualTo(username)));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.username = 'value'} against a repository containing a different username.
     */
    @Test
    void givenUsernameAndDifferentUserInRepository_whenFindAll$usernameIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val user = createTestUser();

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userUsernameIsEqualTo("another")));

        // Then
        assertFalse(returned.contains(user));
    }
}
