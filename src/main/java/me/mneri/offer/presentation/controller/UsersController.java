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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.mneri.offer.business.bean.Paging;
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.business.service.OfferService;
import me.mneri.offer.business.service.UserService;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.presentation.dto.OfferDto;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.dto.UserDto;
import me.mneri.offer.presentation.mapping.PresentationLayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for paths starting with {@code /users}.
 *
 * @author Massimo Neri
 */
@Log4j2
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@Tag(name = "users", description = "The User API")
public class UsersController {
    private final OfferService offerService;

    private final PresentationLayerMapper presentationLayerMapper;

    private final UserService userService;

    /**
     * Retrieve the list of all {@link User}s.
     *
     * @return The list of all users.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation.")})
    @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the list of enabled users.",
            description = "Return the list of enabled users.")
    public Iterable<UserDto> getUsers(@ModelAttribute PagingDto pagingDto) {
        Paging paging = presentationLayerMapper.mapPagingDtoToPaging(pagingDto);
        return presentationLayerMapper.mapUserToUserDto(userService.findAllEnabled(paging));
    }

    /**
     * Retrieve the {@link User} with the specified id.
     *
     * @param userId The id to look for.
     * @return The user.
     * @throws UserNotFoundException The specified user was not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the user doesn't exist or it's disabled.")})
    @GetMapping(value = "/{userId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the user identified by the specified id.",
            description = "Return the user given its id or return an error if such user doesn't exist or it's disabled.")
    public UserDto getUserById(@PathVariable UUID userId) throws UserIsNotEnabledException, UserNotFoundException {
        User user = userService
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        return presentationLayerMapper.mapUserToUserDto(user);
    }

    /**
     * Retrieve the list of {@link Offer}s given a {@link User} id.
     *
     * @param userId The id of the user.
     * @return The list of offers published by the specified user.
     * @throws UserNotFoundException The specified user was not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the user doesn't exist or it's disabled.")})
    @GetMapping(value = "/{userId}/offers", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the list of offers published by the user identified by the specified id.",
            description = "Return a user's offers or return an error if the user doesn't exist or it's disabled.")
    public Iterable<OfferDto> getOffersByPublisherId(@PathVariable UUID userId,
                                                     @ModelAttribute PagingDto pagingDto)
            throws UserIsNotEnabledException, UserNotFoundException {
        Paging paging = presentationLayerMapper.mapPagingDtoToPaging(pagingDto);
        return presentationLayerMapper.mapOfferToOfferDto(offerService.findAllOpenByPublisherId(userId, paging));
    }
}
