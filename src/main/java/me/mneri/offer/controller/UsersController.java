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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;
import static me.mneri.offer.mapping.Types.USER_DTO_LIST_TYPE;

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

    private OfferService offerService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return modelMapper.map(userService.findAllEnabled(), USER_DTO_LIST_TYPE);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable String userId) {
        Optional<User> optional = userService.findEnabledById(userId);

        if (!optional.isPresent()) {
            throw new UserIdNotFoundException(userId);
        }

        return optional.map(user -> modelMapper.map(user, UserDto.class)).get();
    }

    @GetMapping("/{userId}/offers")
    public List<OfferDto> getOffersByPublisherId(@PathVariable String userId) {
        List<Offer> offers = offerService.findAllOpenByPublisherId(userId);
        return modelMapper.map(offers, OFFER_DTO_LIST_TYPE);
    }
}
