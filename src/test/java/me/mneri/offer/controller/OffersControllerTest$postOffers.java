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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.entity.User;
import me.mneri.offer.mapping.OfferMapper;
import me.mneri.offer.service.OfferService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test the {@code POST /offers} endpoint.
 * <p>
 * We test 2 main cases:
 * <ul>
 *     <li>The user is not in the repository;</li>
 *     <li>The user is in the repository.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class OffersControllerTest$postOffers {
    private static final String PATH = "/offers?user.id=%s";
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private OfferMapper offerMapper;

    @MockBean
    private OfferService offerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @BeforeEach
    private void beforeEach() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test the endpoint against an empty repository.
     */
    @SneakyThrows
    @Test
    void givenInvalidUserIdAndOfferPostRequest_whenPostOffersIsCalled_thenHttp404ResponseIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        Optional<User> optionalUser = Optional.empty();
        val userId = user.getId();
        val offer = TestUtil.createNonExpiredOffer(user);
        val offerPostRequest = offerMapper.entityToRequest(offer);

        given(userService.findEnabledById(userId))
                .willReturn(optionalUser);

        // When
        val response = mockMvc
                .perform(post(String.format(PATH, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerPostRequest)))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository which contains the user.
     */
    @SneakyThrows
    @Test
    void givenValidUserIdAndOfferPostRequest_whenPostOffersIsCalled_thenHttp201ResponseIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val optionalUser = Optional.of(user);
        val userId = user.getId();
        val offer = TestUtil.createNonExpiredOffer(user);
        val offerPostRequest = offerMapper.entityToRequest(offer);

        given(userService.findEnabledById(userId))
                .willReturn(optionalUser);

        // When
        val response = mockMvc
                .perform(post(String.format(PATH, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerPostRequest)))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }
}
