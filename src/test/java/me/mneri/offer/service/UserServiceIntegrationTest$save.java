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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the {@link UserService#save(User)}.
 *
 * @author mneri
 */
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceIntegrationTest$save {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test {@link UserService#save(User)} saving a {@link User} and then retrieving it from the repository.
     */
    @Test
    void givenUser_whenSaveIsInvoked_thenUserIsRetrievable() {
        // Given
        val user = new User("user", "secret", passwordEncoder);

        // When
        userService.save(user);

        // Then
        val returned = userRepository.findById(user.getId());
        assertTrue(returned.isPresent());
        assertEquals(user, returned.get());
    }
}
