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
import me.mneri.offer.mapping.OfferMapper;
import me.mneri.offer.service.OfferService;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test the {@code GET /offers/{offerId}} endpoint.
 * <p>
 * We test 2 main cases:
 * <ul>
 *     <li>An empty repository;</li>
 *     <li>A repository containing the specified offer.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class OffersControllerTest$getOfferById {
    private static final String PATH = "/offers/%s";

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private OfferService offerService;

    @BeforeEach
    private void beforeEach() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test the endpoint against an empty repository.
     */
    @SneakyThrows
    @Test
    void givenEmptyRepository_whenGetOfferByIdIsCalled_thenNoOfferIsReturned() {
        // Given
        val id = UUID.randomUUID().toString();
        Optional<Offer> optional = Optional.empty();

        given(offerService.findById(id))
                .willReturn(optional);

        // When
        val response = mvc
                .perform(get(String.format(PATH, id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository containing an offer with the specified offer id.
     */
    @SneakyThrows
    @Test
    void givenEnabledUser_whenGetUsersIsCalled_thenUserIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);
        val id = offer.getId();
        Optional<Offer> optional = Optional.of(offer);

        given(offerService.findById(id))
                .willReturn(optional);

        // When
        val response = mvc
                .perform(get(String.format(PATH, id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        OfferDto result = objectMapper.readValue(response.getContentAsString(), OfferDto.class);
        OfferDto expected = offerMapper.entityToDto(offer);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}
