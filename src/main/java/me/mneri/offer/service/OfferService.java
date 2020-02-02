package me.mneri.offer.service;

import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static me.mneri.offer.specification.OfferSpecification.*;
import static me.mneri.offer.specification.UserSpecification.userIdIsEqualTo;
import static me.mneri.offer.specification.UserSpecification.userIsEnabled;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Service for accessing the offer repository.
 *
 * @author mneri
 */
@Service
public class OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Find all the open {@link Offer}s.
     *
     * @return The list of the open offers.
     */
    public List<Offer> findAllOpen() {
        return offerRepository.findAll(where(offerIsOpen()));
    }

    /**
     * Find all the open {@link Offer}s published by the specified {@link User}.
     *
     * @param id The id of the user.
     * @return The list of the open offers published by the specified user.
     */
    @Transactional
    public List<Offer> findAllOpenByPublisherId(String id) throws UserIdNotFoundException {
        if (userRepository.count(where(userIsEnabled()).and(userIdIsEqualTo(id))) == 0) {
            throw new UserIdNotFoundException(id);
        }

        return offerRepository.findAll(where(offerIsOpen()).and(offerPublisherIdIsEqualTo(id)));
    }

    /**
     * Find all the open {@link Offer}s published by the specified {@link User}.
     *
     * @param username The username of the user.
     * @return The list of the open offers published by the specified user.
     */
    public List<Offer> findAllOpenByPublisherUsername(String username) {
        return offerRepository.findAll(where(offerIsOpen()).and(offerPublisherUsernameIsEqualTo(username)));
    }

    /**
     * Find the {@link Offer} with the specified id.
     *
     * @param id The id of the offer.
     * @return The offer with the specified id.
     */
    public Optional<Offer> findOpenById(String id) {
        return offerRepository.findOne(where(offerIsOpen()).and(offerIdIsEqualTo(id)));
    }

    /**
     * Persist an offer into the database.
     *
     * @param offer The offer.
     */
    public void save(Offer offer) {
        offerRepository.save(offer);
    }
}
