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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.presentation.dto.OfferDto;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.dto.ResponseDto;
import me.mneri.offer.presentation.dto.UserDto;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
@Tag(
        name = "users",
        description = "The User API")
public interface UsersAPI {
    /**
     * Retrieve the list of all {@link User}s.
     *
     * @return The list of all users.
     */
    @Operation(
            summary = "Return the list of enabled users.",
            description = "Return the list of enabled users.",
            parameters = {
                    @Parameter(
                            name = APIParameters.PARAM_PAGE_NUMBER,
                            in = ParameterIn.QUERY,
                            schema = @Schema(
                                    implementation = Integer.class)),
                    @Parameter(
                            name = APIParameters.PARAM_PAGE_SIZE,
                            in = ParameterIn.QUERY,
                            schema = @Schema(
                                    implementation = Integer.class))},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation.")})
    @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    ResponseDto<List<UserDto>> getUsers(@ModelAttribute @Parameter(hidden = true) PagingDto pagingDto);

    /**
     * Retrieve the {@link User} with the specified id.
     *
     * @param userId The id to look for.
     * @return The user.
     * @throws UserNotFoundException The specified user was not found.
     */
    @Operation(
            summary = "Return the user identified by the specified id.",
            description = "Return the user given its id or return an error if disabled or doesn't exist.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "If the user is not enabled.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the user doesn't exist.",
                            content = @Content)})
    @GetMapping(value = "/{userId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    ResponseDto<UserDto> getUserById(@PathVariable UUID userId) throws UserIsNotEnabledException, UserNotFoundException;

    /**
     * Retrieve the list of {@link Offer}s given a {@link User} id.
     *
     * @param userId The id of the user.
     * @return The list of offers published by the specified user.
     * @throws UserNotFoundException The specified user was not found.
     */
    @Operation(
            summary = "Return the list of offers published by the user identified by the specified id.",
            description = "Return a user's offers or return an error if disabled or doesn't exist.",
            parameters = {
                    @Parameter(
                            name = APIParameters.PARAM_PAGE_NUMBER,
                            in = ParameterIn.QUERY,
                            schema = @Schema(
                                    implementation = Integer.class)),
                    @Parameter(
                            name = APIParameters.PARAM_PAGE_SIZE,
                            in = ParameterIn.QUERY,
                            schema = @Schema(
                                    implementation = Integer.class))},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "If the user is not enabled.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the user doesn't exist.",
                            content = @Content)})
    @GetMapping(value = "/{userId}/offers", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    ResponseDto<List<OfferDto>> getOffersByPublisherId(@PathVariable UUID userId,
                                                       @ModelAttribute @Parameter(hidden = true) PagingDto pagingDto)
            throws UserIsNotEnabledException, UserNotFoundException;
}
