package me.mneri.offer.specification;

import lombok.val;
import me.mneri.offer.TestUtil;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static me.mneri.offer.specification.OfferSpecification.offerPublisherIdIsEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link OfferSpecification#offerPublisherIdIsEqualTo(String)} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing an offer published by the specified user;</li>
 *     <li>Repository containing an offer published by another user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
class OfferSpecificationIntegrationTest$publisherIdIsEqualTo {
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
     * Test the SQL predicate {@code user.id = 'value'} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$isCanceledIsCalled_thenNoOfferIsReturn() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);

        // When
        val returned = offerRepository.findAll(where(offerPublisherIdIsEqualTo(publisher.getId())));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.id = 'value'} against a repository containing an offer published by a
     * different user.
     */
    @Test
    void givenOfferPublishedByAnotherUser_whenFindAll$publisherIdIsEqualToIsCalled_thenNoOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(other);

        userRepository.save(other);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(offerPublisherIdIsEqualTo(publisher.getId())));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.id = 'value'} against a repository containing an offer published by the same
     * user.
     */
    @Test
    void givenOfferPublishedByUser_whenFindAll$publisherIdIsEqualToIsCalled_thenOfferIsReturned() {
        // Given
        val publisher = new User("user", "secret", passwordEncoder);
        val offer = TestUtil.createNonExpiredOffer(publisher);

        userRepository.save(publisher);
        offerRepository.save(offer);

        // When
        val returned = offerRepository.findAll(where(offerPublisherIdIsEqualTo(publisher.getId())));

        // Then
        assertEquals(1, returned.size());
        assertEquals(offer, returned.get(0));
    }
}
