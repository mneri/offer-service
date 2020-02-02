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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test the {@link UserService#findEnabledByUsername(String)} method. <br/>
 * We test 4 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a user with different username;</li>
 *     <li>Repository containing a disabled user with the same username;</li>
 *     <li>Repository containing an enabled user with the same username;</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceIntegrationTest$findEnabledByUsername {
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
     * Test the method {@link UserService#findEnabledByUsername(String)} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindByUsernameIsCalled_thenNoUserIsReturned() {
        // Given
        val username = "user";

        // When
        val returned = userService.findEnabledByUsername(username);

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledByUsername(String)} against a repository that does not contain the
     * specified username.
     */
    @Test
    void givenUsernameAndDifferentEnabledUserInRepository_whenFindAll$usernameIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val username = "user";
        val user = new User("another", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userService.findEnabledByUsername(username);

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledByUsername(String)} against a repository that contains the specified
     * username, but the user is disabled.
     */
    @Test
    void givenUsernameAndDisabledUserInRepository_whenFindByUsernameIsCalled_thenNoUserIsReturned() {
        // Given
        val username = "user";
        val user = new User(username, "secret", passwordEncoder);

        user.setEnabled(false);
        userRepository.save(user);

        // When
        val returned = userService.findEnabledByUsername(username);

        // Then
        assertFalse(returned.isPresent());
    }

    /**
     * Test the method {@link UserService#findEnabledByUsername(String)} against a repository that contains the specified
     * username and the user is enabled.
     */
    @Test
    void givenUsernameAndEnabledUserInRepository_whenFindByUsernameIsCalled_thenUserIsReturned() {
        // Given
        val username = "user";
        val user = new User(username, "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userService.findEnabledByUsername(username);

        // Then
        assertEquals(user, returned.orElse(null));
    }
}
