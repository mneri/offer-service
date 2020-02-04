package me.mneri.offer.entity;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link User} class.
 *
 * @author mneri
 */
class UserTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test if the fields are correctly initialized upon construction.
     */
    @Test
    void givenUsernameRawPasswordAndEncoder_whenConstructorIsInvoked_thenAllFieldsAreCorrectlyInitialized() {
        // Given
        val username = "user";
        val rawPassword = "pass";

        // When
        val user = new User(username, rawPassword, passwordEncoder);

        // Then
        assertNotNull(user.getId());
        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches(rawPassword, user.getEncodedPassword()));
        assertTrue(user.isEnabled());
    }

    /**
     * Test if the password is set correctly.
     */
    @Test
    void givenRawPasswordAndEncoder_whenSetEncodedPasswordIsInvoked_thenPasswordIsCorrectlySet() {
        // Given
        val rawPassword = "secret";
        val user = new User("user", "", passwordEncoder);

        // When
        user.setEncodedPassword(rawPassword, passwordEncoder);

        // Then
        assertTrue(passwordEncoder.matches(rawPassword, user.getEncodedPassword()));
    }
}
