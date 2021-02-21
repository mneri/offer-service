/*
 * Copyright 2020 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mneri/offer-service.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.offer.data.validator;

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
 * Test {@link Title} validator.
 * <p>
 * The following tests are performed:
 * <ul>
 *     <li>A {@code null} string;</li>
 *     <li>An empty string;</li>
 *     <li>A short string;</li>
 *     <li>A long string</li>
 * </ul>
 *
 * @author Massimo Neri
 */
class TitleTest {
    private static final int OFFER_TITLE_MAX_LENGTH = 256;

    private static final int OFFER_TITLE_MIN_LENGTH = 1;

    @Data
    @AllArgsConstructor
    private static class Subject {
        @Title
        private String title;
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
     * Test {@link Title} validator against an empty string.
     */
    @Test
    void givenEmptyTitle_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject("");

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link Title} validator against legal string.
     */
    @Test
    void givenLegalTitle_whenValidationOccurs_thenNoErrorsAreProduced() {
        // Given
        val subject = new Subject("user");

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertTrue(violations.isEmpty());
    }

    /**
     * Test {@link Title} validator against a long string.
     */
    @Test
    void givenLongTitle_whenValidationOccurs_thenErrorsAreProduced() {
        val builder = new StringBuilder();

        for (int i = 0; i < OFFER_TITLE_MAX_LENGTH + 1; i++) {
            builder.append("u");
        }

        val subject = new Subject(builder.toString());

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link Title} validator against {@code null}.
     */
    @Test
    void givenNullTitle_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject(null);

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link Title} validator against a short string.
     */
    @Test
    void givenShortTitle_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val builder = new StringBuilder();

        for (int i = 0; i < OFFER_TITLE_MIN_LENGTH - 1; i++) {
            builder.append("u");
        }

        val subject = new Subject(builder.toString());

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }
}
