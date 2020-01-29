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

import static me.mneri.offer.specification.UserSpecification.isEnabled;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /**
     * Test the SQL predicate {@code user.enabled = 1} against a repository containing an enabled user.
     */
    @Test
    void givenEnabledUserInRepository_whenFindAll$isEnabledIsCalled_thenUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(isEnabled());

        // Then
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
        val returned = userRepository.findAll(isEnabled());

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the SQL predicate {@code user.enabled = 1} against a repository containing a single disabled user.
     */
    @Test
    void givenDisabledUserInRepository_whenFindAll$isEnabledIsCalled_thenNoUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        user.setEnabled(false);
        userRepository.save(user);

        // When
        val returned = userRepository.findAll(isEnabled());

        // Then
        assertFalse(returned.contains(user));
    }
}
