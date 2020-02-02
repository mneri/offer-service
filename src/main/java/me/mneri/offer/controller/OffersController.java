package me.mneri.offer.controller;

import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.exception.OfferIdNotFound;
import me.mneri.offer.service.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static me.mneri.offer.mapping.Types.OFFER_DTO_LIST_TYPE;

/**
 * REST controller for paths starting with {@code /offers}.
 *
 * @author mneri
 */
@RestController
public class OffersController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OfferService offerService;

    /**
     * Retrieve all the open {@link Offer}s.
     *
     * @return A list of open offers.
     */
    @GetMapping("/offers")
    public List<OfferDto> getOffers() {
        return modelMapper.map(offerService.findAllOpen(), OFFER_DTO_LIST_TYPE);
    }

    /**
     * Retrieve the {@link Offer} identified by the specified id.
     *
     * @param offerId The id of the offer.
     * @return The offer, if present and still open, {@code null} otherwise.
     */
    @GetMapping("/offers/{offerId}")
    public OfferDto getOfferById(@PathVariable String offerId) {
        Optional<Offer> optional = offerService.findOpenById(offerId);

        if (!optional.isPresent()) {
            throw new OfferIdNotFound(offerId);
        }

        return optional.map(offer -> modelMapper.map(offer, OfferDto.class)).get();
    }
}
