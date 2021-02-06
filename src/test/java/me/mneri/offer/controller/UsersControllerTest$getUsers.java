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

package me.mneri.offer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.User;
import me.mneri.offer.mapping.UserMapper;
import me.mneri.offer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test the {@code GET /users} endpoint.
 * <p>
 * We test two main cases:
 * <ul>
 *     <li>An empty repository;</li>
 *     <li>A repository containing a single user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class UsersControllerTest$getUsers {
    private static final String PATH = "/users";

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    private void beforeEach() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test the endpoint against an empty repository (this test also covers the case where no user in the repository is
     * enabled).
     */
    @SneakyThrows
    @Test
    void givenEmptyRepository_whenGetUsersIsCalled_thenNoUserIsReturned() {
        // Given
        List<User> users = Collections.emptyList();

        given(userService.findAllEnabled())
                .willReturn(users);

        // When
        val response = mvc
                .perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        val result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<UserDto>>() {});
        val expected = Collections.emptyList();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }

    /**
     * Test the endpoint against a repository containing a single enabled user.
     */
    @SneakyThrows
    @Test
    void givenEnabledUser_whenGetUsersIsCalled_thenUserIsReturned() {
        // Given
        val users = Collections.singletonList(new User("user", "secret", passwordEncoder));

        given(userService.findAllEnabled())
                .willReturn(users);

        // When
        val response = mvc
                .perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        val result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<UserDto>>() {});
        val expected = userMapper.entityToDto(users);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}
