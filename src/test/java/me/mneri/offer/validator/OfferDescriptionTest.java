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

/**
 * Test {@link OfferDescription} validator.
 * <p>
 * The following tests are performed:
 * <ul>
 *     <li>A {@code null} string;</li>
 *     <li>An empty string;</li>
 *     <li>A short string;</li>
 *     <li>A long string</li>
 * </ul>
 *
 * @author mneri
 */
class OfferDescriptionTest {
    private static final int OFFER_DESCRIPTION_MAX_LENGTH = 8192;
    private static final int OFFER_DESCRIPTION_MIN_LENGTH = 1;

    @Data
    @AllArgsConstructor
    private static class Subject {
        @OfferTitle
        private String description;
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

    /**
     * Test {@link OfferDescription} validator against an empty string.
     */
    @Test
    void givenEmptyDescription_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject("");

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link OfferDescription} validator against legal string.
     */
    @Test
    void givenLegalDescription_whenValidationOccurs_thenNoErrorsAreProduced() {
        // Given
        val subject = new Subject("user");

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertTrue(violations.isEmpty());
    }

    /**
     * Test {@link OfferDescription} validator against a long string.
     */
    @Test
    void givenLongDescription_whenValidationOccurs_thenErrorsAreProduced() {
        val builder = new StringBuilder();

        for (int i = 0; i < OFFER_DESCRIPTION_MAX_LENGTH + 1; i++) {
            builder.append("u");
        }

        val subject = new Subject(builder.toString());

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link OfferDescription} validator against {@code null}.
     */
    @Test
    void givenNullDescription_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject(null);

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link OfferDescription} validator against a short string.
     */
    @Test
    void givenShortDescription_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val builder = new StringBuilder();

        for (int i = 0; i < OFFER_DESCRIPTION_MIN_LENGTH - 1; i++) {
            builder.append("u");
        }

        val subject = new Subject(builder.toString());

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }
}
