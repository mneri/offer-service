package me.mneri.offer.service;

import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;
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
     * Update the specified {@link Offer} given the specified user id.
     * <p>
     * Only the publisher of a specific offer is granted the permission to update.
     *
     * @param offer  The offer.
     * @param userId The user id of the modifier.
     * @throws UserIdNotFoundException    If a user with the specified id was not found in the repository.
     * @throws UserNotAuthorizedException If the specified user id doesn't belong to the publisher of the offer.
     */
    @Transactional
    public void update(Offer offer, String userId) throws UserIdNotFoundException, UserNotAuthorizedException {
        if (userRepository.count(where(userIsEnabled()).and(userIdIsEqualTo(userId))) == 0) {
            throw new UserIdNotFoundException(userId);
        }

        if (offerRepository.count(where(offerIdIsEqualTo(offer.getId())).and(offerPublisherIdIsEqualTo(userId))) == 0) {
            throw new UserNotAuthorizedException(userId);
        }

        offerRepository.save(offer);
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
