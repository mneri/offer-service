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
import me.mneri.offer.mapping.Types;
import me.mneri.offer.service.OfferService;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test the {@code GET /offers} endpoint.
 * <p>
 * We test 2 main cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>A repository containing a single offer.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class OffersControllerTest$getOffers {
    private static final String PATH = "/offers";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private OfferService offerService;

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
    void givenEmptyRepository_whenGetOffersIsCalled_thenNoOfferIsReturned() {
        // Given
        List<Offer> offers = Collections.emptyList();

        given(offerService.findAllOpen())
                .willReturn(offers);

        // When
        val response = mvc
                .perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        val result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {
        });
        val expected = Collections.emptyList();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }

    /**
     * Test the endpoint against a repository containing a single open offer.
     */
    @SneakyThrows
    @Test
    void givenOpenOffer_whenGetOffersIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);
        val offers = Collections.singletonList(offer);

        given(offerService.findAllOpen())
                .willReturn(offers);

        // When
        val response = mvc
                .perform(get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        val result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {
        });
        val expected = modelMapper.map(offers, Types.OFFER_DTO_LIST_TYPE);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}
