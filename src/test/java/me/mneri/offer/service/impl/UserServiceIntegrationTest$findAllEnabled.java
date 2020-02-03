package me.mneri.offer.service.impl;

import lombok.val;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.UserService;
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
 * Test the {@link UserService#findEnabledById(String)} method. <br/>
 * We test 3 different cases:
 * <ul>
 *     <li>Empty repository;</li>
 *     <li>Repository containing a disabled user;</li>
 *     <li>Repository containing a single enabled user.</li>
 * </ul>
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceIntegrationTest$findAllEnabled {
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
     * Test the method {@link UserService#findAllEnabled()} against a repository that contains a disabled user.
     */
    @Test
    void givenDisabledUserInRepository_whenFindAllEnabledIsCalled_thenNoUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);
        user.setEnabled(false);

        userRepository.save(user);

        // When
        val returned = userService.findAllEnabled();

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the method {@link UserService#findAllEnabled()} against an empty repository.
     */
    @Test
    void givenEmptyRepository_whenFindAllEnabledIsCalled_thenNoUserIsReturned() {
        // Given
        // Empty repository

        // When
        val returned = userService.findAllEnabled();

        // Then
        assertTrue(returned.isEmpty());
    }

    /**
     * Test the method {@link UserService#findAllEnabled()} against a repository that contains a single enabled user.
     */
    @Test
    void givenEnabledUserInRepository_whenFindAllEnabledIsCalled_thenUserIsReturned() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val list = userService.findAllEnabled();

        // Then
        assertEquals(1, list.size());

        val returned = list.get(0);

        assertEquals(user.getId(), returned.getId());
        assertEquals(user.getUsername(), returned.getUsername());
        assertEquals(user.getEncodedPassword(), returned.getEncodedPassword());
        assertEquals(user.isEnabled(), returned.isEnabled());
    }
}
