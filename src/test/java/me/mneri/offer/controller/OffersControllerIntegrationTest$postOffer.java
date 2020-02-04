package me.mneri.offer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.dto.OfferRequest;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static me.mneri.offer.specification.OfferSpecification.offerPublisherIdIsEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Integration tests for the method {@link OffersController#postOffer(OfferRequest, String)}.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class OffersControllerIntegrationTest$postOffer {
    private static final String PATH = "/offers?user.id=%s";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

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
     * Post an offer and checks if it has been inserted into the repository.
     */
    @SneakyThrows
    @Test
    void givenOffer_whenPostOfferIsCalled_thenOfferIsPresentInRepository() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = ControllerTestUtil.createTestOffer(publisher);
        val offerRequest = modelMapper.map(offer, OfferRequest.class);

        userRepository.save(publisher);

        // When
        mockMvc.perform(post(String.format(PATH, publisher.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerRequest)));

        // Then
        val result = offerRepository.findOne(offerPublisherIdIsEqualTo(publisher.getId())).orElseThrow(RuntimeException::new);
        assertEquals(offer.getTitle(), result.getTitle());
        assertEquals(offer.getDescription(), result.getDescription());
        assertEquals(offer.getPrice(), result.getPrice());
        assertEquals(offer.getCurrency(), result.getCurrency());
        assertEquals(offer.getTtl(), result.getTtl());
    }
}
