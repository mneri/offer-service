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
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Test the {@code PUT /offers/{offerId}} endpoint.
 * <p>
 * We test 4 main cases:
 * <ul>
 *     <li>The offer is not in the repository;</li>
 *     <li>The offer is in the repository and the specified user is the publisher;</li>
 *     <li>The offer is in the repository and the specified user is not the publisher;</li>
 *     <li>The specified user is not found in the repository.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class OffersControllerTest$putOffer {
    private static final String PATH = "/offers/%s?user.id=%s";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

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
     * Test the endpoint against a repository that doesn't contain the offer.
     */
    @SneakyThrows
    @Test
    void givenEmptyRepository_whenPostOffersIsCalled_thenHttp404ResponseIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val userId = user.getId();
        val offer = TestUtil.createNonExpiredOffer(user);
        val offerId = offer.getId();
        Optional<Offer> optionalOffer = Optional.empty();
        val offerDto = modelMapper.map(offer, OfferDto.class);

        given(offerService.findOpenById(offerId))
                .willReturn(optionalOffer);

        // When
        val response = mockMvc
                .perform(put(String.format(PATH, offerId, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository containing the specified offer but not the specified user.
     */
    @SneakyThrows
    @Test
    void givenOfferAndWrongUserId_whenDeleteOfferIsCalled_thenHttp404ResponseIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val userId = user.getId();
        val offer = TestUtil.createNonExpiredOffer(user);
        val offerId = offer.getId();
        val optionalOffer = Optional.of(offer);
        val offerDto = modelMapper.map(offer, OfferDto.class);

        given(offerService.findOpenById(offerId))
                .willReturn(optionalOffer);

        doThrow(new UserIdNotFoundException(userId))
                .when(offerService).update(offer, userId);

        // When
        val response = mockMvc
                .perform(delete(String.format(PATH, offerId, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository containing the specified offer published by another user.
     */
    @SneakyThrows
    @Test
    void givenOfferPublishedByAnotherUser_whenDeleteOfferIsCalled_thenHttp401ResponseIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val userId = user.getId();
        val offer = TestUtil.createNonExpiredOffer(user);
        val offerId = offer.getId();
        val optionalOffer = Optional.of(offer);
        val offerDto = modelMapper.map(offer, OfferDto.class);

        given(offerService.findOpenById(offerId))
                .willReturn(optionalOffer);

        doThrow(new UserNotAuthorizedException(userId))
                .when(offerService).update(offer, userId);

        // When
        val response = mockMvc
                .perform(delete(String.format(PATH, offerId, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository containing the specified offer published by the specified user.
     */
    @SneakyThrows
    @Test
    void givenOfferPublishedByUser_whenDeleteOfferIsCalled_thenHttp200ResponseIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val userId = user.getId();
        val offer = TestUtil.createNonExpiredOffer(user);
        val offerId = offer.getId();
        val optionalOffer = Optional.of(offer);
        val offerDto = modelMapper.map(offer, OfferDto.class);

        given(offerService.findOpenById(offerId))
                .willReturn(optionalOffer);

        // When
        val response = mockMvc
                .perform(delete(String.format(PATH, offerId, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
