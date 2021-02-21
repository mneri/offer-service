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

package me.mneri.offer.data.entity;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Unit tests for {@link User} class.
 *
 * @author Massimo Neri
 */
class UserTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforeEach() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Test if the password is set correctly.
     */
    @Test
    void givenRawPasswordAndEncoder_whenSetEncodedPasswordIsInvoked_thenPasswordIsCorrectlySet() {
        // Given
        val rawPassword = "secret";
        val user = new User();

        // When
        user.setEncodedPassword(rawPassword, passwordEncoder);

        // Then
        Assertions.assertThat(passwordEncoder.matches(rawPassword, user.getEncodedPassword())).isTrue();
    }

    @Test
    void givenRawPasswordAndEncoder_whenToStringIsCalled_thenPasswordIsNotShown() {
        // Given
        val rawPassword = "secret";

        val user = new User();
        user.setEncodedPassword(rawPassword, passwordEncoder);

        // When
        val actual = user.toString();

        // Then
        Assertions.assertThat(actual.contains(passwordEncoder.encode(rawPassword))).isFalse();
    }

    @Test
    public void givenNullId_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val user = newUserInstance();
        user.setId(null);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullUsername_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val user = newUserInstance();
        user.setUsername(null);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullPassword_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val user = newUserInstance();
        user.setEncodedPassword(null);

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    private User newUserInstance() {
        User user = new User();
        user.setId("00000000-0000-0000-0000-000000000000");
        user.setUsername("user");
        user.setEncodedPassword("secret", passwordEncoder);
        return user;
    }
}
