package me.mneri.offer.repository;

import lombok.val;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test {@link OfferRepository} against {@link Offer} constraints.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
class OfferRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OfferRepository offerRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Create a test {@link Offer}.
     *
     * @param publisher The {@link User} who published the offer.
     * @return The offer.
     */
    private Offer createLegitTestOffer(User publisher) {
        return Offer.builder()
                .title("Bazinga")
                .description("Amazing")
                .price(new BigDecimal("100.00"))
                .currency("GBP")
                .ttl(30 * 24 * 60 * 60 * 1000L)
                .publisher(publisher)
                .build();
    }

    /**
     * Create a test {@link User}.
     *
     * @return A new user.
     */
    private User createTestPublisher() {
        return new User("user", "secret", passwordEncoder);
    }

    /**
     * Test {@link OfferRepository#save(Object)} against an {@link Offer} with blank description.
     */
    @Test
    void givenBlankDescription_whenOfferIsPersisted_thenConstraintViolationExceptionIsThrown() {
        // Given
        val publisher = createTestPublisher();
        val offer = createLegitTestOffer(publisher);

        testEntityManager.persist(publisher);
        offer.setDescription("");

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            offerRepository.save(offer);
            testEntityManager.flush();
        });
    }

    /**
     * Test {@link OfferRepository#save(Object)} against an {@link Offer} with blank title.
     */
    @Test
    void givenBlankTitle_whenOfferIsPersisted_thenConstraintViolationExceptionIsThrown() {
        // Given
        val publisher = createTestPublisher();
        val offer = createLegitTestOffer(publisher);

        testEntityManager.persist(publisher);
        offer.setTitle("");

        // When/Then
        assertThrows(ConstraintViolationException.class, () -> {
            offerRepository.save(offer);
            testEntityManager.flush();
        });
    }

    @Test
    void givenLegitOffer_whenOfferIsPersisted_thenNoExceptionIsThrown() {
        // Given
        val publisher = createTestPublisher();
        val offer = createLegitTestOffer(publisher);

        testEntityManager.persist(publisher);

        // When
        offerRepository.save(offer);
        testEntityManager.flush();

        // Then
        // No exceptions.
    }
}
