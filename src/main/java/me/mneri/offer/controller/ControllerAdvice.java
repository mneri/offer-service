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

package me.mneri.offer.controller;

import me.mneri.offer.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * General controller advice.
 *
 * @author mneri
 */
@RestControllerAdvice
public class ControllerAdvice {
    /**
     * Handler for {@link OfferIsCancelledException}.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The specified offer was cancelled.")
    @ExceptionHandler(OfferIsCancelledException.class)
    public void offerIsCancelled() {
    }

    /**
     * Handler for {@link OfferIsExpiredException}.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The specified offer is expired.")
    @ExceptionHandler(OfferIsExpiredException.class)
    public void offerIsExpired() {
    }

    /**
     * Handler for {@link OfferNotFoundException}.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified offer id was not found.")
    @ExceptionHandler(OfferNotFoundException.class)
    public void offerNotFound() {
    }

    /**
     * Handler for {@link UserNotFoundException}.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified user id was not found.")
    @ExceptionHandler(UserNotFoundException.class)
    public void userNotFound() {
    }

    /**
     * Handler for {@link UserNotAuthorizedException}.
     */
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The user has no rights to modify the resource.")
    @ExceptionHandler(UserNotAuthorizedException.class)
    public void userNotAuthorizedException() {
    }
}
