package me.mneri.offer.mapping;

import lombok.val;
import me.mneri.offer.dto.OfferPostRequest;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OfferPostRequestToOfferMappingTest {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private OfferPostRequest createTestOfferPostRequest() {
        OfferPostRequest request = new OfferPostRequest();
        request.setTitle("Bazinga");
        request.setDescription("Awesome");
        request.setPrice(new BigDecimal("100.00"));
        request.setCurrency("GBP");
        request.setEnd(new Date(currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L));
        return request;
    }

    private User createTestPublisher() {
        return new User("user", "secret", passwordEncoder);
    }

    @Test
    void givenOfferPostRequest_whenOfferPostRequestIsMappedToOffer_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val request = createTestOfferPostRequest();

        // When
        val offer = modelMapper.map(request, Offer.class);

        // Then
        assertNotNull(offer.getId());
        assertEquals(request.getTitle(), offer.getTitle());
        assertEquals(request.getDescription(), offer.getDescription());
        assertEquals(request.getPrice(), offer.getPrice());
        assertEquals(request.getCurrency(), offer.getCurrency());
        assertEquals(request.getEnd(), offer.getEnd());
    }
}
