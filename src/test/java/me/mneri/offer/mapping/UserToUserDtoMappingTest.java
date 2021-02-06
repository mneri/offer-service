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

package me.mneri.offer.mapping;

import lombok.val;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link User} to {@link UserDto} mapping.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
class UserToUserDtoMappingTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Test the correct initialization of all the fields after the mapping.
     */
    @Test
    void givenUser_whenUserIsMappedToUserDto_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        // When
        val dto = userMapper.entityToDto(user);

        // Then
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUsername(), dto.getUsername());
    }
}
