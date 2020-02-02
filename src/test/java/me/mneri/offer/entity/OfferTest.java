package me.mneri.offer.entity;

import lombok.val;
import me.mneri.offer.util.TextUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link Offer} class.
 *
 * @author mneri
 */
class OfferTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the creation of a new {@link Offer} instance.
     */
    @Test
    void givenAllParameters_whenNewInstanceIsConstructed_thenAllFieldsAreProperlyInitialized() {
        // Given
        val title = "Bazinga";
        val description = "Awesome";
        val price = new BigDecimal("100.00");
        val currency = "GBP";
        val end = new Date(currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L);
        val publisher = new User("user", "secret", passwordEncoder);

        // When
        val instance = Offer.builder()
                .title(title)
                .description(description)
                .price(price)
                .currency(currency)
                .end(end)
                .publisher(publisher)
                .build();

        // Then
        assertFalse(TextUtil.isEmpty(instance.getId()));
        assertEquals(title, instance.getTitle());
        assertEquals(description, instance.getDescription());
        assertEquals(0, price.compareTo(instance.getPrice()));
        assertEquals(currency, instance.getCurrency());
        assertEquals(end, instance.getEnd());
        assertFalse(instance.isCanceled());
        assertEquals(publisher, instance.getPublisher());
    }
}
