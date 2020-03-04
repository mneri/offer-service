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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the {@link UserService#save(User)}.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest$save {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test {@link UserService#save(User)} saving a {@link User} and then retrieving it from the repository.
     */
    @Test
    void givenUser_whenSaveIsInvoked_thenUserIsRetrievable() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        // When
        userService.save(user);

        // Then
        val optional = userRepository.findById(user.getId());
        assertTrue(optional.isPresent());

        val returned = optional.get();
        assertEquals(user.getId(), returned.getId());
        assertEquals(user.getUsername(), returned.getUsername());
        assertEquals(user.getEncodedPassword(), returned.getEncodedPassword());
        assertEquals(user.isEnabled(), returned.isEnabled());
    }
}
