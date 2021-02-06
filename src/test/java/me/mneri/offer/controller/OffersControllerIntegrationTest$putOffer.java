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
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Integration tests for the method {@link OffersController#putOffer(String, me.mneri.offer.dto.OfferUpdateDto, String)}.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class OffersControllerIntegrationTest$putOffer {
    private static final String PATH = "/offers/%s?user.id=%s";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void beforeEach() {
        objectMapper = new ObjectMapper();
    }


    /**
     * Modify an offer and checks if modification were correct.
     */
    @SneakyThrows
    @Test
    void givenOffer_whenPostOfferIsCalled_thenOfferIsPresentInRepository() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);
        val offerRequest = TestUtil.createOfferUpdateDto();

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        mockMvc.perform(put(String.format(PATH, offer.getId(), publisher.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest)));

        // Then
        val result = offerRepository.findById(offer.getId()).orElseThrow(RuntimeException::new);
        assertEquals(offerRequest.getTitle(), result.getTitle());
        assertEquals(offerRequest.getDescription(), result.getDescription());
        assertEquals(offerRequest.getPrice(), result.getPrice());
        assertEquals(offerRequest.getCurrency(), result.getCurrency());
        assertEquals(offerRequest.getTtl(), result.getTtl());
    }
}
