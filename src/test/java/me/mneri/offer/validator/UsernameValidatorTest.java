package me.mneri.offer.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsernameValidatorTest {
    @Data
    @AllArgsConstructor
    private static class Subject {
        @Username
        private String username;
    }

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void givenEmptyUsername_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject("");

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenLegalUsername_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject("user");

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenNullUsername_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject(null);

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenShortUsername_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject("uu");

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenUsernameWithIllegalCharacters_whenValidationOccurs_thenErrorsAreProduced() {
        String illegals = "`¬¦!\"£$%^&*()-=+[];'#,./{}:@~<>?\\|";

        for (char c : illegals.toCharArray()) {
            // Given
            val subject = new Subject("user" + c);

            // When
            Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

            // Then
            assertFalse(violations.isEmpty());
        }
    }
}
