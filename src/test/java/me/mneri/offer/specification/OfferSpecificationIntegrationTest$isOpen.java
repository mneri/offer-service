package me.mneri.offer.specification;

import lombok.val;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static me.mneri.offer.specification.OfferSpecification.isOpen;
import static me.mneri.offer.specification.OfferSpecificationTestUtil.createExpiredTestOffer;
import static me.mneri.offer.specification.OfferSpecificationTestUtil.createNonExpiredTestOffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link OfferSpecification#isOpen()} specification.<br/>
 * We test 5 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a non-canceled and non-expired offer;</li>
 *     <li>Repository containing a non-canceled but expired offer;</li>
 *     <li>Repository containing a canceled but non-expired offer;</li>
 *     <li>Repository containing a canceled and expired offer;</li>
 * </ul>
 *
 * @author mneri
 */
@DataJpaTest
@ExtendWith(SpringExtension.class)
class OfferSpecificationIntegrationTest$isOpen {
    @Autowired
    private OfferRepository offerRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void givenCanceledAndExpiredOffer_whenFindAll$isOpenIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = createExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offer.setCanceled(true);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(isOpen()));

        // Then
        assertTrue(returned.isEmpty());
    }

    @Test
    void givenCanceledButNonExpiredOffer_whenFindAll$isOpenIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = createNonExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offer.setCanceled(true);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(isOpen()));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the {@link OfferSpecification#isOpen()} specification against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$isOpenIsCalled_thenNoOfferIsReturn() {
        // Given
        // Empty repository

        // When
        val returned = offerRepository.findAll(where(isOpen()));

        // Then
        assertTrue(returned.isEmpty());
    }

    @Test
    void givenNonCanceledAndNonExpiredOffer_whenFindAll$isOpenIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = createNonExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(isOpen()));

        // Then
        assertEquals(1, returned.size());
        assertTrue(returned.contains(offer));
    }

    @Test
    void givenNonCanceledButExpiredOffer_whenFindAll$isOpenIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = createExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(isOpen()));

        // Then
        assertTrue(returned.isEmpty());
    }
}
