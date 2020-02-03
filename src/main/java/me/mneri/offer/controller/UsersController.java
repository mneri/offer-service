package me.mneri.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;
import static me.mneri.offer.mapping.Types.USER_DTO_LIST_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * REST controller for paths starting with {@code /users}.
 *
 * @author mneri
 */
@RequestMapping("/users")
@RestController
@Tag(name = "users", description = "The User API")
public class UsersController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    /**
     * Retrieve the list of all {@link User}s.
     *
     * @return The list of all users.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation.")})
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the list of enabled users.",
               description = "Return the list of enabled users.")
    public List<UserDto> getUsers() {
        return modelMapper.map(userService.findAllEnabled(), USER_DTO_LIST_TYPE);
    }

    /**
     * Retrieve the {@link User} with the specified id.
     *
     * @param userId The id to look for.
     * @return The user.
     * @throws UserIdNotFoundException The specified user was not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the user doesn't exist or it's disabled.")})
    @GetMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the user identified by the specified id.",
               description = "Return the user given its id or return an error if such user doesn't exist or it's disabled.")
    public UserDto getUserById(@PathVariable String userId) throws UserIdNotFoundException {
        Optional<User> optional = userService.findEnabledById(userId);

        if (!optional.isPresent()) {
            throw new UserIdNotFoundException(userId);
        }

        return optional.map(user -> modelMapper.map(user, UserDto.class)).get();
    }

    /**
     * Retrieve the list of {@link Offer}s given a {@link User} id.
     *
     * @param userId The id of the user.
     * @return The list of offers published by the specified user.
     * @throws UserIdNotFoundException The specified user was not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation."),
            @ApiResponse(responseCode = "404", description = "If the user doesn't exist or it's disabled.")})
    @GetMapping(value = "/{userId}/offers", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Return the list of offers published by the user identified by the specified id.",
               description = "Return a user's offers or return an error if the user doesn't exist or it's disabled.")
    public List<OfferDto> getOffersByPublisherId(@PathVariable String userId) throws UserIdNotFoundException {
        List<Offer> offers = offerService.findAllOpenByPublisherId(userId);
        return modelMapper.map(offers, OFFER_DTO_LIST_TYPE);
    }
}
