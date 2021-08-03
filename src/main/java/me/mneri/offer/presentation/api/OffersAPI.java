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

package me.mneri.offer.presentation.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.mneri.offer.business.exception.OfferIsCancelledException;
import me.mneri.offer.business.exception.OfferIsExpiredException;
import me.mneri.offer.business.exception.OfferNotFoundException;
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotAuthorizedException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.presentation.dto.OfferCreateDto;
import me.mneri.offer.presentation.dto.OfferDto;
import me.mneri.offer.presentation.dto.OfferUpdateDto;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/offers")
@Tag(name = "offers", description = "The Offer API")
public interface OffersAPI {
    /**
     * Close (cancel) an existing open offer.
     * <p>
     * The deletion of an offer is only logical: the offer cancelled field is set to true and the object physically
     * remains inside the repository and it will be filtered out by query predicates.
     *
     * @param offerId The offer id.
     * @param auth    The id of the user attempting the modification.
     * @throws OfferIsCancelledException  If the offer with the specified id was previously cancelled.
     * @throws OfferIsExpiredException    If the offer with the specified id has expired.
     * @throws OfferNotFoundException     If the offer with the specified id was not found in the repository.
     * @throws UserIsNotEnabledException  If the user with the specified id is not enabled.
     * @throws UserNotFoundException      If a user with the specified id was not found in the repository.
     * @throws UserNotAuthorizedException If the specified user id doesn't belong to the publisher of the offer.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "401", description = "If the user has no rights to modify the offer."),
            @ApiResponse(responseCode = "404", description = "If the user or the offer don't exist or they are not enabled.")})
    @DeleteMapping(value = "/{offerId}")
    @Operation(summary = "Delete an open offer.",
            description = "Delete an open offer in the repository.")
    void deleteOffer(@PathVariable UUID offerId, @RequestParam(APIParameters.PARAM_AUTH_TOKEN) UUID auth)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotFoundException, UserNotAuthorizedException;

    /**
     * Retrieve all the open {@link Offer}s. An open offer is an offer that is not yet expired nor has been canceled
     * by its publisher.
     *
     * @return A list of open offers.
     */
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Successful operation."))
    @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the list of open offers.",
            description = "Return the list of the non-expired, non-canceled offers.")
    Iterable<OfferDto> getOffers(@ModelAttribute PagingDto pagingDto);

    /**
     * Retrieve the {@link Offer} identified by the specified id, if open. An open offer is an offer that is not yet
     * expired nor has been canceled by its publisher.
     *
     * @param offerId The id of the offer.
     * @return The offer, if present and still open, {@code null} otherwise.
     * @throws OfferIsCancelledException If the specified offer was cancelled.
     * @throws OfferIsExpiredException   If the specified offer is expired.
     * @throws OfferNotFoundException    If the specified offer id was not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the offer doesn't exist or it's closed.")})
    @GetMapping(value = "/{offerId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the offer identified by the specified id.",
            description = "Return the offer given its id or return an error if such offer doesn't exist or it's closed.")
    OfferDto getOfferById(@PathVariable UUID offerId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException;

    /**
     * Return the {@link User} who published the offer identified by the specified id, if open. An open offer is an
     * offer that is not yet expired nor has been canceled by its publisher.
     *
     * @param offerId The offer id.
     * @return The user.
     * @throws OfferIsCancelledException If the specified offer was cancelled.
     * @throws OfferIsExpiredException   If the specified offer is expired.
     * @throws OfferNotFoundException    If the specified offer id was not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the offer doesn't exist or it's closed.")})
    @GetMapping(value = "/{offerId}/user", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the user who published the offer identified by the specified id.",
            description = "Return the user given an offer id or return an error if such offer doesn't exist or it's closed.")
    UserDto getUserByOfferId(@PathVariable UUID offerId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException;

    /**
     * Create a new {@link Offer}.
     *
     * @param createDto The offer.
     * @param auth      The id of the user attempting the creation.
     * @throws UserIsNotEnabledException If the user with the specified id is not enabled.
     * @throws UserNotFoundException     If a user with the specified id was not found in the repository.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the user doesn't exist or it's not enabled.")})
    @Operation(summary = "Insert a new open offer.",
            description = "Insert a new open offer in the repository.")
    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    OfferDto postOffer(@Valid @RequestBody OfferCreateDto createDto,
                       @RequestParam(APIParameters.PARAM_AUTH_TOKEN) UUID auth)
            throws UserIsNotEnabledException, UserNotFoundException;

    /**
     * Modify the specified {@link Offer}.
     *
     * @param offerId   The offer id.
     * @param updateDto The update data.
     * @param auth      The user id of the publisher of the offer.
     * @throws OfferIsCancelledException  If the offer with the specified id was previously cancelled.
     * @throws OfferIsExpiredException    If the offer with the specified id has expired.
     * @throws OfferNotFoundException     If the offer with the specified id was not found in the repository.
     * @throws UserIsNotEnabledException  If the user with the specified id is not enabled.
     * @throws UserNotFoundException      If a user with the specified id was not found in the repository.
     * @throws UserNotAuthorizedException If the specified user id doesn't belong to the publisher of the offer.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "401", description = "If the user has no rights to modify the offer."),
            @ApiResponse(responseCode = "404", description = "If the user or the offer don't exist or they are not enabled.")})
    @Operation(summary = "Modify an open offer.",
            description = "Modify an open offer in the repository.")
    @PutMapping(value = "/{offerId}", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    void putOffer(@PathVariable UUID offerId,
                  @Valid @RequestBody OfferUpdateDto updateDto,
                  @RequestParam(APIParameters.PARAM_AUTH_TOKEN) UUID auth)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotFoundException, UserNotAuthorizedException;
}
