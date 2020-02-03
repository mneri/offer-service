package me.mneri.offer.service.impl;

import lombok.SneakyThrows;
import lombok.val;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the {@link OfferService#update(Offer, String)}.
 * <p>
 * We test 3 different cases:
 * <ul>
 *     <li>The user is not the publisher of the offer;</li>
 *     <li>The offer id is not in the repository;</li>
 *     <li>The user is the publisher and the offer id is in the repository.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class OfferServiceIntegrationTest$update {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test {@link OfferService#update(Offer, String)} given a user id that is different from the id of the offer's
     * publisher.
     */
    @Test
    void givenWrongUserId_whenUpdateIsInvoked_thenOfferUserNotAuthorizedExceptionIsThrown() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offer = OfferServiceTestUtil.createNonExpiredTestOffer(publisher);

        userRepository.save(publisher);
        userRepository.save(other);
        offerRepository.save(offer);

        // When/Then
        assertThrows(UserNotAuthorizedException.class, () -> {
            offerService.update(offer, other.getId());
        });
    }

    /**
     * Test {@link OfferService#update(Offer, String)} given a user id that is not present in the repository.
     */
    @Test
    void givenNonExistingUserId_whenUpdateIsInvoked_thenUserIdNotFoundExceptionIsThrown() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val otherId = UUID.randomUUID().toString();
        val offer = OfferServiceTestUtil.createNonExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When/Then
        assertThrows(UserIdNotFoundException.class, () -> {
            offerService.update(offer, otherId);
        });
    }

    /**
     * Test {@link OfferService#update(Offer, String)} given the proper user id.
     */
    @SneakyThrows
    @Test
    void givenUserId_whenUpdateIsInvoked_thenOfferIsUpdatedThrown() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = OfferServiceTestUtil.createNonExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        offer.setTitle("New title");
        offer.setDescription("New description");
        offer.setPrice(new BigDecimal("999.99"));
        offer.setCurrency("EUR");
        offer.setEnd(new Date(currentTimeMillis() + 10000L));

        // When
        offerService.update(offer, publisher.getId());

        // Then
        val optional = offerRepository.findById(offer.getId());

        assertTrue(optional.isPresent());

        val returned = optional.get();

        assertEquals(offer.getTitle(), returned.getTitle());
        assertEquals(offer.getDescription(), returned.getDescription());
        assertEquals(offer.getPrice(), returned.getPrice());
        assertEquals(offer.getCurrency(), returned.getCurrency());
        assertEquals(offer.getEnd(), returned.getEnd());
    }
}
