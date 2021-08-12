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
import me.mneri.offer.test.answer.UnsupportedOperationAnswer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Unit tests for {@link Offer} class.
 *
 * @author Massimo Neri
 */
class OfferTest {
    @Test
    public void givenEndTime_whenGetTtlIsCalled_thenCorrectTtlIsReturned() {
        // Given
        val now = Date.from(Instant.parse("2020-01-01T08:00:00Z"));

        val clock = Mockito.mock(Clock.class, new UnsupportedOperationAnswer());
        Mockito.doReturn(now.getTime()).when(clock).millis();

        val endTime = Date.from(Instant.parse("2020-01-31T22:00:00.00Z"));

        val offer = new Offer();
        offer.setEndTime(endTime);

        // When
        val actual = offer.getTtl(clock);

        // Then
        val expected = endTime.getTime() - now.getTime();

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void givenExpiredOffer_whenIsExpiredIsCalled_thenTrueIsReturned() {
        // Given
        val now = Date.from(Instant.parse("2020-02-01T08:00:00Z"));

        val clock = Mockito.mock(Clock.class, new UnsupportedOperationAnswer());
        Mockito.doReturn(now.getTime()).when(clock).millis();

        val endTime = Date.from(Instant.parse("2020-01-31T22:00:00.00Z"));

        val offer = new Offer();
        offer.setEndTime(endTime);

        // When
        val actual = offer.isExpired(clock);

        // Then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    public void givenNonExpiredOffer_whenIsExpiredIsCalled_thenFalseIsReturned() {
        // Given
        val now = Date.from(Instant.parse("2020-01-01T08:00:00Z"));

        val clock = Mockito.mock(Clock.class, new UnsupportedOperationAnswer());
        Mockito.doReturn(now.getTime()).when(clock).millis();

        val endTime = Date.from(Instant.parse("2020-01-31T22:00:00.00Z"));

        val offer = new Offer();
        offer.setEndTime(endTime);

        // When
        val actual = offer.isExpired(clock);

        // Then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    public void givenClockAndTtl_whenSetTtlIsCalled_thenGetEndTimeReturnsCorrectValue() {
        // Given
        val now = Date.from(Instant.parse("2020-01-01T08:00:00Z"));
        val ttl = 60 * 1000L;
        val endTime = new Date(now.getTime() + ttl);

        val clock = Mockito.mock(Clock.class, new UnsupportedOperationAnswer());
        Mockito.doReturn(now.getTime()).when(clock).millis();

        val offer = new Offer();

        // When
        offer.setTtl(ttl, clock);

        // Then
        Assertions.assertThat(offer.getEndTime()).isEqualTo(endTime);
    }

    @Test
    public void givenZeroTtl_whenSetTtlIsCalled_thenIllegalArgumentExceptionIsThrown() {
        // Given
        val clock = Mockito.mock(Clock.class, new UnsupportedOperationAnswer());

        val ttl = 0L;

        val offer = new Offer();

        // When/Then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> offer.setTtl(ttl, clock));
    }

    @Test
    public void givenNegativeTtl_whenSetTtlIsCalled_thenIllegalArgumentExceptionIsThrown() {
        // Given
        val clock = Mockito.mock(Clock.class, new UnsupportedOperationAnswer());

        val ttl = -1L;

        val offer = new Offer();

        // When/Then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> offer.setTtl(ttl, clock));
    }

    @Test
    public void givenNullCreateTime_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val offer = newOfferInstance();
        offer.setCreateTime(null);

        // When
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullCurrency_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val offer = newOfferInstance();
        offer.setCurrency(null);

        // When
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullDescription_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val offer = newOfferInstance();
        offer.setDescription(null);

        // When
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullEndTime_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val offer = newOfferInstance();
        offer.setEndTime(null);

        // When
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullId_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val offer = newOfferInstance();
        offer.setId(null);

        // When
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullPrice_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val offer = newOfferInstance();
        offer.setPrice(null);

        // When
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void givenNullTitle_whenValidationOccurs_thenErrorsAreProduced() {
        // Given
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        val offer = newOfferInstance();
        offer.setTitle(null);

        // When
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);

        // Then
        Assertions.assertThat(violations.isEmpty()).isFalse();
    }

    private Offer newOfferInstance() {
        val offer = new Offer();

        offer.setCreateTime(Date.from(Instant.parse("2020-01-01T08:00:00.00Z")));
        offer.setCurrency("GBP");
        offer.setDescription("Buy one and get one free!");
        offer.setEndTime(Date.from(Instant.parse("2020-01-31T22:00:00.00Z")));
        offer.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        offer.setPrice(new BigDecimal("0.00"));
        offer.setTitle("Free Coffee");

        return offer;
    }
}
