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

import static me.mneri.offer.specification.UserSpecification.usernameIsEqualTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserSpecificationIntegrationTest$usernameIsEqualTo {
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void givenUsernameAndUserInRepository_whenFindAll$usernameIsEqualToIsCalled_thenUserIsReturned() {
        // Given
        val username = "user";
        val user = new User(username, "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(usernameIsEqualTo(username));

        // Then
        assertTrue(returned.contains(user));
    }

    @Test
    void givenUsernameAndEmptyRepository_whenFindAll$usernameIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val username = "user";

        // When
        val returned = userRepository.findAll(usernameIsEqualTo(username));

        // Then
        assertTrue(returned.isEmpty());
    }

    @Test
    void givenUsernameAndDifferentUserInRepository_whenFindAll$usernameIsEqualToIsCalled_thenNoUserIsReturned() {
        // Given
        val username = "user";
        val user = new User("another", "secret", passwordEncoder);

        userRepository.save(user);

        // When
        val returned = userRepository.findAll(usernameIsEqualTo(username));

        // Then
        assertFalse(returned.contains(user));
    }
}
