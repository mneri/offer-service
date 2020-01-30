package me.mneri.offer.controller;

import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.OfferPostRequest;
import me.mneri.offer.dto.UserDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.OfferIdNotFound;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.service.OfferService;
import me.mneri.offer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;
import static me.mneri.offer.mapping.Types.USER_DTO_LIST_TYPE;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * Main application REST controller.
 *
 * @author mneri
 */
@RestController
public class MainController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @GetMapping("/offers")
    public List<OfferDto> getOffers() {
        return modelMapper.map(offerService.findAllOpen(), OFFER_DTO_LIST_TYPE);
    }

    @GetMapping("/offers/{offerId}")
    public OfferDto getOfferById(@PathVariable String offerId) {
        if (!offerService.existsOpenById(offerId)) {
            throw new OfferIdNotFound(offerId);
        }

        Optional<Offer> optional = offerService.findOpenById(offerId);
        return optional.map(offer -> modelMapper.map(offer, OfferDto.class)).orElse(null);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return modelMapper.map(userService.findEnabled(), USER_DTO_LIST_TYPE);
    }

    @GetMapping("/users/{userId}")
    public UserDto getUserById(@PathVariable String userId) {
        if (!userService.existsEnabledById(userId)) {
            throw new UserIdNotFoundException(userId);
        }

        Optional<User> optional = userService.findEnabledById(userId);
        return optional.map(user -> modelMapper.map(user, UserDto.class)).orElse(null);
    }

    @GetMapping("/users/{userId}/offers")
    public List<OfferDto> getOffersByPublisherId(@PathVariable String userId) {
        if (!userService.existsEnabledById(userId)) {
            throw new UserIdNotFoundException(userId);
        }

        List<Offer> offers = offerService.findAllOpenByPublisherId(userId);
        return modelMapper.map(offers, OFFER_DTO_LIST_TYPE);
    }

    @PostMapping("/users/{userId}/offers")
    public void postOfferByUserId(@PathVariable String userId, @Valid @RequestBody OfferPostRequest body, HttpServletResponse response) {
        Optional<User> optional = userService.findEnabledById(userId);

        if (!optional.isPresent()) {
            throw new UserIdNotFoundException(userId);
        }

        Offer offer = modelMapper.map(body, Offer.class);
        offer.setPublisher(optional.get());
        offerService.save(offer);

        response.setStatus(CREATED.value());
    }
}
