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

import static me.mneri.offer.specification.OfferSpecification.offerIsCanceled;
import static me.mneri.offer.specification.OfferSpecificationTestUtil.createNonExpiredTestOffer;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link OfferSpecification#offerIsCanceled()} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a canceled offer;</li>
 *     <li>Repository containing a single non-canceled offer.</li>
 * </ul>
 *
 * @author mneri
 */
@DataJpaTest
@ExtendWith(SpringExtension.class)
class OfferSpecificationIntegrationTest$isCanceled {
    @Autowired
    private OfferRepository offerRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the SQL predicate {@code offer.canceled = 1} against a repository containing a canceled offer.
     */
    @Test
    void givenCanceledOffer_whenFindAll$isCanceledIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = createNonExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offer.setCanceled(true);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(offerIsCanceled()));

        // Then
        assertFalse(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code offer.canceled = 1} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$isCanceledIsCalled_thenNoOfferIsReturn() {
        // Given
        // Empty repository

        // When
        val returned = offerRepository.findAll(where(offerIsCanceled()));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code offer.canceled = 1} against a repository containing a single enabled offer.
     */
    @Test
    void givenEnabledOffer_whenFindAll$isCanceledIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = createNonExpiredTestOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(offerIsCanceled()));

        // Then
        assertTrue(returned.isEmpty());
    }
}
