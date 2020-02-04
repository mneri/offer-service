package me.mneri.offer.controller;

import me.mneri.offer.exception.OfferIdNotFoundException;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;
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
     * Handler for {@link OfferIdNotFoundException}.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified offer id was not found.")
    @ExceptionHandler(OfferIdNotFoundException.class)
    public void offerIdNotFound() {
    }

    /**
     * Handler for {@link UserIdNotFoundException}.
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The specified user id was not found.")
    @ExceptionHandler(UserIdNotFoundException.class)
    public void userIdNotFound() {
    }

    /**
     * Handler for {@link UserNotAuthorizedException}.
     */
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The user has no rights to modify the resource.")
    @ExceptionHandler(UserNotAuthorizedException.class)
    public void userNotAuthorizedException() {
    }
}
