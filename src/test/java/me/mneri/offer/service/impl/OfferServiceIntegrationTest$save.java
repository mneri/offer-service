package me.mneri.offer.service.impl;

import lombok.val;
import me.mneri.offer.TestUtil;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the {@link OfferService#save(Offer)}.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class OfferServiceIntegrationTest$save {
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
     * Test {@link OfferService#save(Offer)} saving a {@link Offer} and then retrieving it from the repository.
     */
    @Test
    void givenOffer_whenSaveIsInvoked_thenOfferIsRetrievable() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);

        // When
        offerService.save(offer);

        // Then
        val optional = offerRepository.findById(offer.getId());

        assertTrue(optional.isPresent());

        val returned = optional.get();

        assertEquals(offer.getId(), returned.getId());
        assertEquals(offer.getTitle(), returned.getTitle());
        assertEquals(offer.getDescription(), returned.getDescription());
        assertEquals(offer.getPrice(), returned.getPrice());
        assertEquals(offer.getCurrency(), returned.getCurrency());
        assertEquals(offer.getEndTime(), returned.getEndTime());
    }
}
