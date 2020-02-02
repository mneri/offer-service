package me.mneri.offer.controller;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
public class OffersControllerTest$getOfferById {
    private static final String PATH = "/offers/%s";

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

    private Offer createTestOffer(User publisher) {
        return Offer.builder()
                .title("Bazinga")
                .description("Amazing")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .publisher(publisher)
                .end(new Date(System.currentTimeMillis() + 30 * 42 * 60 * 60 * 1000L))
                .build();
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

        given(offerService.findOpenById(id))
                .willReturn(optional);

        // When
        val response = mvc
                .perform(get(String.format(PATH, id))
                        .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        assertEquals(NOT_FOUND.value(), response.getStatus());
    }

    /**
     * Test the endpoint against a repository containing an offer with the specified offer id.
     */
    @SneakyThrows
    @Test
    void givenEnabledUser_whenGetUsersIsCalled_thenUserIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = createTestOffer(publisher);
        val id = offer.getId();
        Optional<Offer> optional = Optional.of(offer);

        given(offerService.findOpenById(id))
                .willReturn(optional);

        // When
        val response = mvc
                .perform(get(String.format(PATH, id))
                        .contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        OfferDto result = objectMapper.readValue(response.getContentAsString(), OfferDto.class);
        OfferDto expected = modelMapper.map(offer, OfferDto.class);

        assertEquals(OK.value(), response.getStatus());
        assertEquals(expected, result);
    }
}
