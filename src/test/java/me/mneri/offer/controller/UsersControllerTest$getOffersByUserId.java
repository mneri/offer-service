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
import me.mneri.offer.TestUtil;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test the {@code GET /users/{userId}/offers} endpoint.
 * <p>
 * We test 3 main cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>A repository containing a user without any open offer;</li>
 *     <li>A repository containing a user with a single offer.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class UsersControllerTest$getOffersByUserId {
    private static final String PATH = "/users/%s/offers";

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
    void givenNoUsers_whenGetOffersByUserIdIsCalled_thenHttp404ResponseIsReturned() {
        // Given
        val userId = UUID.randomUUID().toString();

        given(offerService.findAllOpenByPublisherId(userId))
                .willThrow(new UserIdNotFoundException(userId));

        // When
        val response = mockMvc
                .perform(get(String.format(PATH, userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository containing a user without offers.
     */
    @SneakyThrows
    @Test
    void givenUserAndNoOffers_whenGetOffersByUserIdIsCalled_thenEmptyResultIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val userId = user.getId();
        List<Offer> offers = Collections.emptyList();

        given(offerService.findAllOpenByPublisherId(userId))
                .willReturn(offers);

        // When
        val response = mockMvc
                .perform(get(String.format(PATH, userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        Iterable<OfferDto> expected = offerMapper.entityToDto(offers);
        List<OfferDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {});

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }

    /**
     * Test the endpoint against a repository containig a user with a single offer.
     */
    @SneakyThrows
    @Test
    void givenUserAndOffers_whenGetOffersByUserIdIsCalled_thenOffersAreReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val userId = user.getId();
        List<Offer> offers = Collections.singletonList(TestUtil.createNonExpiredOffer(user));

        given(offerService.findAllOpenByPublisherId(userId))
                .willReturn(offers);

        // When
        val response = mockMvc
                .perform(get(String.format(PATH, userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        Iterable<OfferDto> expected = offerMapper.entityToDto(offers);
        List<OfferDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {});

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}
