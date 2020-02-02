package me.mneri.offer.specification;

import lombok.val;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static me.mneri.offer.specification.UserSpecification.userIsEnabled;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Test the {@link UserSpecification#userIsEnabled()} specification.<br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing an enabled user;</li>
 *     <li>Repository containing a disabled user.</li>
 * </ul>
 *
 * @author mneri
 */
@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserSpecificationIntegrationTest$isEnabled {
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    private User createTestUser() {
        return new User("user", "secret", passwordEncoder);
    }

    /**
     * Test the SQL predicate {@code user.enabled = 1} against a repository containing an enabled user.
     */
    @Test
    void givenEnabledUserInRepository_whenFindAll$isEnabledIsCalled_thenUserIsReturned() {
        // Given
        val user = createTestUser();

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userIsEnabled()));

        // Then
        assertEquals(1, returned.size());
        assertTrue(returned.contains(user));
    }

    /**
     * Test the SQL predicate {@code user.enabled = 1} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAll$isEnabledIsCalled_thenNoUserIsReturned() {
        // Given
        // Empty repository

        // When
        val returned = userRepository.findAll(where(userIsEnabled()));

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.enabled = 1} against a repository containing a single disabled user.
     */
    @Test
    void givenDisabledUserInRepository_whenFindAll$isEnabledIsCalled_thenNoUserIsReturned() {
        // Given
        val user = createTestUser();

        user.setEnabled(false);
        userRepository.save(user);

        // When
        val returned = userRepository.findAll(where(userIsEnabled()));

        // Then
        assertTrue(returned.isEmpty());
    }
}
