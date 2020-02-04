package me.mneri.offer.mapping;

import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Offer} to {@link OfferDto} mapping.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
class OfferToOfferDtoMappingTest {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a test {@link User}.
     *
     * @return The user.
     */
    private User createTestPublisher() {
        return new User("user", "secret", passwordEncoder);
    }

    /**
     * Test the correct initialization of all the fields after the mapping.
     */
    @Test
    void givenOffer_whenOfferIsMappedToOfferDto_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val publisher = createTestPublisher();
        val offer = TestUtil.createNonExpiredOffer(publisher);

        // When
        val dto = modelMapper.map(offer, OfferDto.class);

        // Then
        assertEquals(offer.getId(), dto.getId());
        assertEquals(offer.getTitle(), dto.getTitle());
        assertEquals(offer.getDescription(), dto.getDescription());
        assertEquals(offer.getPrice(), dto.getPrice());
        assertEquals(offer.getCurrency(), dto.getCurrency());
        assertEquals(offer.getTtl(), dto.getTtl());
    }
}
