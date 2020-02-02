package me.mneri.offer.service;

import lombok.val;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test the {@link UserService#findEnabledById(String)} method. <br/>
 * We test 4 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a user with different id;</li>
 *     <li>Repository containing a disabled user with the same id;</li>
 *     <li>Repository containing an enabled user with the same id;</li>
 * </ul>
 *
 * @author mneri
 */
@SpringBootTest
@Transactional
class UserServiceIntegrationTest$findEnabledById {
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test the method {@link UserService#findEnabledById(String)} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindByUsernameIsCalled_thenNoUserIsReturned() {
        // Given
        val id = UUID.randomUUID().toString();

        // When
        val returned = userService.findEnabledById(id);

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledById(String)} against a repository that does not contain the
     * specified id.
     */
    @Test
    void givenUsernameAndDifferentEnabledUserInRepository_whenFindAll$usernameIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        val other = new User("other", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userService.findEnabledById(other.getId());

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledById(String)} against a repository that contains the specified id,
     * but the user is disabled.
     */
    @Test
    void givenUsernameAndDisabledUserInRepository_whenFindByUsernameIsCalled_thenNoUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        user.setEnabled(false);
        userRepository.save(user);

        // When
        val returned = userService.findEnabledById(user.getId());

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledById(String)} against a repository that contains the specified
     * username and the user is enabled.
     */
    @Test
    void givenUsernameAndEnabledUserInRepository_whenFindByUsernameIsCalled_thenUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userService.findEnabledById(user.getId());

        // Then
        assertEquals(user, returned.orElse(null));
    }
}
