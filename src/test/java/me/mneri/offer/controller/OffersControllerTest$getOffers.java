package me.mneri.offer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;
import static me.mneri.offer.mapping.Types.USER_DTO_LIST_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test the {@code GET /offers} endpoint.
 *
 * @author mneri
 */
@AutoConfigureMockMvc
@SpringBootTest
public class OffersControllerTest$getOffers {
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
    void givenEmptyRepository_whenGetUsersIsCalled_thenNoUserIsReturned() {
        // Given
        List<Offer> offers = Collections.emptyList();

        given(offerService.findAllOpen())
                .willReturn(offers);

        // When
        val response = mvc
                .perform(get(PATH)
                        .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        val result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {});
        val expected = Collections.emptyList();

        assertEquals(OK.value(), response.getStatus());
        assertEquals(expected, result);
    }

    /**
     * Test the endpoint against a repository containing a single enabled user.
     */
    @SneakyThrows
    @Test
    void givenEnabledUser_whenGetUsersIsCalled_thenUserIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = Offer.builder()
                .title("Bazinga")
                .description("Amazing")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .publisher(publisher)
                .end(new Date(System.currentTimeMillis() + 30 * 24* 60 * 60 *1000L))
                .build();
        val offers = Collections.singletonList(offer);

        given(offerService.findAllOpen())
                .willReturn(offers);

        // When
        val response = mvc
                .perform(get(PATH)
                        .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        val result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<OfferDto>>() {});
        val expected = modelMapper.map(offers, OFFER_DTO_LIST_TYPE);

        assertEquals(OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}
