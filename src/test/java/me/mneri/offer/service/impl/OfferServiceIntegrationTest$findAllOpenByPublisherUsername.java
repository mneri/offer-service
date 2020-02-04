package me.mneri.offer.service.impl;

import lombok.val;
import me.mneri.offer.TestUtil;
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
 * Test the {@link OfferService#findAllOpenByPublisherUsername(String)}.<br/>
 * We test 5 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing only closed offers by the specified user;</li>
 *     <li>Repository containing only closed offers by another user;</li>
 *     <li>Repository containing a single open offer by the specified user;</li>
 *     <li>Repository containing a single open offer by another user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OfferServiceIntegrationTest$findAllOpenByPublisherUsername {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherUsername(String)} against an repository containing only
     * closed offers published by the specified user.
     */
    @Test
    void givenClosedOffersPublishedByUser_whenFindAllOpenByPublisherUsernameIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offers = TestUtil.createClosedOfferList(publisher);

        userRepository.save(publisher);

        for (val offer : offers) {
            offerRepository.save(offer);
        }

        // When
        val returned = offerService.findAllOpenByPublisherUsername(publisher.getUsername());

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherUsername(String)} against an repository containing only
     * closed offers published by another user.
     */
    @Test
    void givenClosedOffersPublishedByAnotherUser_whenFindAllOpenByPublisherUsernameIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offers = TestUtil.createClosedOfferList(other);

        userRepository.save(other);

        for (val offer : offers) {
            offerRepository.save(offer);
        }

        // When
        val returned = offerService.findAllOpenByPublisherUsername(publisher.getUsername());

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherUsername(String)} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAllOpenByPublisherUsernameIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);

        // When
        val returned = offerService.findAllOpenByPublisherUsername(publisher.getUsername());

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherUsername(String)} against an repository containing a
     * single open offers published by another user.
     */
    @Test
    void givenOpenOfferPublishedByAnotherUser_whenFindAllOpenByPublisherUsernameIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(other);

        userRepository.save(other);
        offerRepository.save(offer);

        // When
        val returned = offerService.findAllOpenByPublisherUsername(publisher.getUsername());

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the method {@link OfferService#findAllOpenByPublisherUsername(String)} against an repository containing a
     * single open offers published by the specified user.
     */
    @Test
    void givenOpenOfferPublishedByUser_whenFindAllOpenByPublisherUsernameIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerService.findAllOpenByPublisherUsername(publisher.getUsername());

        // Then
        assertEquals(1, returned.size());
        assertTrue(returned.contains(offer));
    }
}
