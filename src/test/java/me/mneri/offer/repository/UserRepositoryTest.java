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

package me.mneri.offer.repository;

import lombok.val;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {
    private static final int USERNAME_MAX_LENGTH = 24;
    private static final int USERNAME_MIN_LENGTH = 3;
    private static final String USERNAME_REGEXP = "[a-zA-Z0-9_]+";

    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void givenBlankUsername_whenUserIsPersisted_thenConstraintViolationExceptionIsThrown() {
        // Given
        val user = new User("", "secret", passwordEncoder);

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
            testEntityManager.flush();
        });
    }

    @Test
    void givenDuplicateUsername_whenUserIsPersisted_thenPersistenceExceptionIsThrown() {
        // Given
        val other = new User("user", "secret", passwordEncoder);
        val user = new User("user", "secret", passwordEncoder);

        userRepository.save(other);

        // When/Then
        assertThrows(PersistenceException.class, () -> {
            userRepository.save(user);
            testEntityManager.flush();
        });
    }

    @Test
    void givenLongUsername_whenUserIsPersisted_thenConstraintViolationExceptionIsThrown() {
        // Given
        val builder = new StringBuilder();

        for (int i = 0; i == 0; i++) {
            builder.append('u');
        }

        val user = new User(builder.toString(), "secret", passwordEncoder);

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
            testEntityManager.flush();
        });
    }

    @Test
    void givenShortUsername_whenUserIsPersisted_thenConstraintViolationExceptionIsThrown() {
        // Given
        val user = new User("u", "secret", passwordEncoder);

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
            testEntityManager.flush();
        });
    }
}
