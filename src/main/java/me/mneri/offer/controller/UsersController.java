package me.mneri.offer.controller;

import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;
import static me.mneri.offer.mapping.Types.USER_DTO_LIST_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * REST controller for paths starting with {@code /users}.
 *
 * @author mneri
 */
@RequestMapping("/users")
@RestController
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
    @GetMapping
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
    @GetMapping("/{userId}")
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
    @GetMapping("/{userId}/offers")
    public List<OfferDto> getOffersByPublisherId(@PathVariable String userId) throws UserIdNotFoundException {
        List<Offer> offers = offerService.findAllOpenByPublisherId(userId);
        return modelMapper.map(offers, OFFER_DTO_LIST_TYPE);
    }

    /**
     * Handler for {@link UserIdNotFoundException}.
     */
    @ResponseStatus(value = NOT_FOUND, reason = "The specified user id was not found.")
    @ExceptionHandler(UserIdNotFoundException.class)
    public void userIdNotFound() {
    }
}
