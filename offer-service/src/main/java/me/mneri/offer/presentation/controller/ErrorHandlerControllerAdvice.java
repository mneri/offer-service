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

package me.mneri.offer.presentation.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.mneri.offer.business.exception.OfferIsCancelledException;
import me.mneri.offer.business.exception.OfferIsExpiredException;
import me.mneri.offer.business.exception.OfferNotFoundException;
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotAuthorizedException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.presentation.dto.ErrorDto;
import me.mneri.offer.presentation.exception.IllegalPageSizeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * General controller advice for error handling.
 *
 * @author Massimo Neri
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RestControllerAdvice
class ErrorHandlerControllerAdvice {
    /**
     * Handler for {@link IllegalPageSizeException}
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalPageSizeException.class)
    public ErrorDto illegalPageSize(IllegalPageSizeException exception) {
        log.info("The specified page size is not legal: pageSize={}", exception.getPageSize());
        return new ErrorDto(exception.getMessage());
    }

    /**
     * Handler for {@link OfferIsCancelledException}.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OfferIsCancelledException.class)
    public ErrorDto offerIsCancelled(OfferIsCancelledException exception) {
        log.info("The specified offer is cancelled: offerId={}", exception.getOfferId());
        return new ErrorDto(exception.getMessage());
    }

    /**
     * Handler for {@link OfferIsExpiredException}.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OfferIsExpiredException.class)
    public ErrorDto offerIsExpired(OfferIsExpiredException exception) {
        log.info("The specified offer is expired: offerId={}", exception.getOfferId());
        return new ErrorDto(exception.getMessage());
    }

    /**
     * Handler for {@link OfferNotFoundException}.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(OfferNotFoundException.class)
    public ErrorDto offerNotFound(OfferNotFoundException exception) {
        log.info("The specified offer was not found: offerId={}", exception.getOfferId());
        return new ErrorDto(exception.getMessage());
    }

    /**
     * Handler for {@link UserIsNotEnabledException}.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserIsNotEnabledException.class)
    public ErrorDto userIsNotEnabled(UserIsNotEnabledException exception) {
        log.info("The specified user was not found: userId={}", exception.getUserId());
        return new ErrorDto(exception.getMessage());
    }

    /**
     * Handler for {@link UserNotFoundException}.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorDto userNotFound(UserNotFoundException exception) {
        log.info("The specified user was not found: userId={}", exception.getUserId());
        return new ErrorDto(exception.getMessage());
    }

    /**
     * Handler for {@link UserNotAuthorizedException}.
     */
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserNotAuthorizedException.class)
    public ErrorDto userNotAuthorizedException(UserNotAuthorizedException exception) {
        log.info("The specified user is not authorized: userId={}", exception.getUserId());
        return new ErrorDto(exception.getMessage());
    }
}
