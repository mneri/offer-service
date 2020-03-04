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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.OfferRequest;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.OfferIdNotFoundException;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;
import me.mneri.offer.mapping.Types;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for paths starting with {@code /offers}.
 *
 * @author mneri
 */
@Log4j2
@RequestMapping("/offers")
@RestController
@Tag(name = "offers",
     description = "The Offer API")
public class OffersController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

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
    public List<OfferDto> getOffers() {
        return modelMapper.map(offerService.findAllOpen(), Types.OFFER_DTO_LIST_TYPE);
    }

    /**
     * Retrieve the {@link Offer} identified by the specified id, if open. An open offer is an offer that is not yet
     * expired nor has been canceled by its publisher.
     *
     * @param offerId The id of the offer.
     * @return The offer, if present and still open, {@code null} otherwise.
     * @throws OfferIdNotFoundException The specified offer id was not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the offer doesn't exist or it's closed.")})
    @GetMapping(value = "/{offerId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the user identified by the specified id.",
               description = "Return the offer given its id or return an error if such offer doesn't exist or it's closed.")
    public OfferDto getOfferById(@PathVariable String offerId) throws OfferIdNotFoundException {
        Optional<Offer> optional = offerService.findOpenById(offerId);

        if (!optional.isPresent()) {
            log.debug("No open offer with the specified id was found; offerId: {}", offerId);
            throw new OfferIdNotFoundException(offerId);
        }

        return optional.map(offer -> modelMapper.map(offer, OfferDto.class)).get();
    }

    /**
     * Create a new {@link Offer}.
     *
     * @param request The offer.
     * @param userId  The user id of the publisher of the offer.
     * @throws UserIdNotFoundException If the user is not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the user doesn't exist or it's not enabled.")})
    @Operation(summary = "Insert a new open offer.",
               description = "Insert a new open offer in the repository.")
    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void postOffer(@Valid @RequestBody OfferRequest request, @RequestParam("user.id") String userId)
            throws UserIdNotFoundException {
        Optional<User> optional = userService.findEnabledById(userId);

        if (!optional.isPresent()) {
            log.debug("No enabled user with the specified id was found; userId: {}", userId);
            throw new UserIdNotFoundException(userId);
        }

        Offer offer = modelMapper.map(request, Offer.class);
        offer.setPublisher(optional.get());

        offerService.save(offer);
    }

    /**
     * Modify the specified {@link Offer}.
     *
     * @param request The offer.
     * @param userId  The user id of the publisher of the offer.
     * @throws UserIdNotFoundException    If the user has no rights to modify the offer.
     * @throws UserNotAuthorizedException If the user is not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "401", description = "If the user has no rights to modify the offer."),
            @ApiResponse(responseCode = "404", description = "If the user or the offer don't exist or they are not enabled.")})
    @Operation(summary = "Modify an open offer.",
               description = "Modify an open offer in the repository.")
    @PutMapping(value = "/{offerId}", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public void putOffer(@PathVariable String offerId, @Valid @RequestBody OfferRequest request, @RequestParam("user.id") String userId)
            throws OfferIdNotFoundException, UserIdNotFoundException, UserNotAuthorizedException {
        Optional<Offer> offerOptional = offerService.findOpenById(offerId);

        if (!offerOptional.isPresent()) {
            log.debug("No open offer with the specified id was found; offerId: {}", offerId);
            throw new OfferIdNotFoundException(offerId);
        }

        Offer offer = offerOptional.get();
        modelMapper.map(request, offer);

        offerService.update(offer, userId);
    }

    /**
     * Close (cancel) an existing open offer.
     *
     * The deletion of an offer is only logical: the offer cancelled field is set to true and the object physically
     * remains inside the repository and it will be filtered out by query predicates.
     *
     * @param offerId The offer id.
     * @param userId  The id of the user attempting the modification.
     * @throws OfferIdNotFoundException   If an offer with the specified id is not found.
     * @throws UserIdNotFoundException    If an user with the specified id is not found.
     * @throws UserNotAuthorizedException If the user is not the publisher of the specified offer.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "401", description = "If the user has no rights to modify the offer."),
            @ApiResponse(responseCode = "404", description = "If the user or the offer don't exist or they are not enabled.")})
    @Operation(summary = "Delete an open offer.",
               description = "Delete an open offer in the repository.")
    @DeleteMapping(value = "/{offerId}", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public void deleteOffer(@PathVariable String offerId, @RequestParam("user.id") String userId)
            throws OfferIdNotFoundException, UserIdNotFoundException, UserNotAuthorizedException {
        Optional<Offer> offerOptional = offerService.findOpenById(offerId);

        if (!offerOptional.isPresent()) {
            log.debug("No open offer with the specified id was found; offerId: {}", offerId);
            throw new OfferIdNotFoundException(offerId);
        }

        Offer offer = offerOptional.get();
        offer.setCanceled(true);

        offerService.update(offer, userId);
    }
}
