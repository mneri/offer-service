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

package me.mneri.offer.presentation.validator;

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
 * Test {@link Ttl} validator.
 * <p>
 * The following tests are performed:
 * <ul>
 *     <li>A zero value;</li>
 *     <li>An negative value;</li>
 *     <li>A positive value;</li>
 * </ul>
 *
 * @author Massimo Neri
 */
class TtlTest {
    @Data
    @AllArgsConstructor
    private static class Subject {
        @Ttl
        private long ttl;
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
     * Test {@link Ttl} validator against an zero value.
     */
    @Test
    void givenZeroTtl_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject(0L);

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link Ttl} validator against a negative value.
     */
    @Test
    void givenNegativeTtl_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        val subject = new Subject(-1L);

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertFalse(violations.isEmpty());
    }

    /**
     * Test {@link Ttl} validator against a positive value.
     */
    @Test
    void givenPositiveTtl_whenValidationOccurs_thenNoErrorIsProduced() {
        // Given
        val subject = new Subject(1L);

        // When
        Set<ConstraintViolation<Subject>> violations = validator.validate(subject);

        // Then
        assertTrue(violations.isEmpty());
    }
}
