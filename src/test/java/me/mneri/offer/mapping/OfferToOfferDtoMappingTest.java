package me.mneri.offer.mapping;

import lombok.val;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OfferToOfferDtoMappingTest {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Offer createTestOffer(User publisher) {
        return Offer.builder()
                .title("Bazinga")
                .description("Amazing")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .end(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L))
                .publisher(publisher)
                .build();
    }

    private User createTestPublisher() {
        return new User("user", "secret", passwordEncoder);
    }

    @Test
    void givenOffer_whenOfferIsMappedToOfferDto_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val publisher = createTestPublisher();
        val offer = createTestOffer(publisher);

        // When
        val dto = modelMapper.map(offer, OfferDto.class);

        // Then
        assertEquals(offer.getId(), dto.getId());
        assertEquals(offer.getTitle(), dto.getTitle());
        assertEquals(offer.getDescription(), dto.getDescription());
        assertEquals(offer.getPrice(), dto.getPrice());
        assertEquals(offer.getCurrency(), dto.getCurrency());
        assertEquals(offer.getEnd(), dto.getEnd());
    }
}
