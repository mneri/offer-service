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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link UserSpec#idIsEqualTo(String)} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a user with the specified id;</li>
 *     <li>Repository containing a user with a different id.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserSpecificationIntegrationTest$idIsEqualTo {
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
     * Test the SQL predicate {@code user.id = 'value'} against a repository containing the wanted id.
     */
    @Test
    void givenIdAndUserInRepository_whenFindAll$idIsEqualToIsCalled_thenUserIsReturned() {
        // Given
        val user = createTestUser();

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userSpec.idIsEqualTo(user.getId())));

        // Then
        assertTrue(returned.contains(user));
    }

    /**
     * Test the SQL predicate {@code user.id = 'value'} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$idIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val id = UUID.randomUUID().toString();

        // When
        val returned = userRepository.findAll(where(userSpec.idIsEqualTo(id)));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.id = 'value'} against a repository containing a different id.
     */
    @Test
    void givenUsernameAndDifferentUserInRepository_whenFindAll$idIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val user = createTestUser();
        val id = UUID.randomUUID().toString();

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userSpec.idIsEqualTo(id)));

        // Then
        assertFalse(returned.contains(user));
    }
}
