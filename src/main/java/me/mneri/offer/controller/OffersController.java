package me.mneri.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.OfferPostRequest;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.exception.OfferIdNotFoundException;
import me.mneri.offer.service.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * REST controller for paths starting with {@code /offers}.
 *
 * @author mneri
 */
@RequestMapping(consumes = APPLICATION_JSON_VALUE,
                produces = APPLICATION_JSON_VALUE,
                value = "/offers")
@RestController
@Tag(name = "offers", description = "The Offer API")
public class OffersController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OfferService offerService;

    /**
     * Retrieve all the open {@link Offer}s. An open offer is an offer that is not yet expired nor has been canceled
     * by its publisher.
     *
     * @return A list of open offers.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation.")})
    @GetMapping
    @Operation(summary = "Return the list of open offers.",
               description = "Return the list of the non-expired, non-canceled offers.")
    public List<OfferDto> getOffers() {
        return modelMapper.map(offerService.findAllOpen(), OFFER_DTO_LIST_TYPE);
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
    @GetMapping("/{offerId}")
    @Operation(summary = "Return the user identified by the specified id.",
               description = "Return the offer given its id or return an error if such offer doesn't exist or it's closed.")
    public OfferDto getOfferById(@PathVariable String offerId) throws OfferIdNotFoundException {
        Optional<Offer> optional = offerService.findOpenById(offerId);

        if (!optional.isPresent()) {
            throw new OfferIdNotFoundException(offerId);
        }

        return optional.map(offer -> modelMapper.map(offer, OfferDto.class)).get();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void postOffer(@Valid @RequestBody OfferPostRequest request) {
    }

    @PutMapping
    public void putOffer(@Valid @RequestBody OfferPostRequest request) {
    }

    /**
     * Handler for {@link OfferIdNotFoundException}.
     */
    @ResponseStatus(value = NOT_FOUND, reason = "The specified offer id was not found.")
    @ExceptionHandler(OfferIdNotFoundException.class)
    public void offerIdNotFound() {
    }
}
