package me.mneri.offer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
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
@AutoConfigureMockMvc
@SpringBootTest
public class UsersControllerTest$getOffersByUserId {
    private static final String PATH = "/users/%s/offers";

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
        assertEquals(NOT_FOUND.value(), response.getStatus());
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
        List<OfferDto> expected = modelMapper.map(offers, OFFER_DTO_LIST_TYPE);
        List<OfferDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {});

        assertEquals(OK.value(), response.getStatus());
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
        List<Offer> offers = Collections.singletonList(ControllerTestUtil.createTestOffer(user));

        given(offerService.findAllOpenByPublisherId(userId))
                .willReturn(offers);

        // When
        val response = mockMvc
                .perform(get(String.format(PATH, userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        List<OfferDto> expected = modelMapper.map(offers, OFFER_DTO_LIST_TYPE);
        List<OfferDto> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {});

        assertEquals(OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}
