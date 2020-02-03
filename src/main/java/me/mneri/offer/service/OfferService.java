package me.mneri.offer.service;

import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;

import java.util.List;
import java.util.Optional;

/**
 * Service for accessing the offer repository.
 *
 * @author mneri
 */
public interface OfferService {
    /**
     * Find all the open {@link Offer}s.
     *
     * @return The list of the open offers.
     */
    List<Offer> findAllOpen();

    /**
     * Find all the open {@link Offer}s published by the specified {@link User}.
     *
     * @param id The id of the user.
     * @return The list of the open offers published by the specified user.
     */
    List<Offer> findAllOpenByPublisherId(String id) throws UserIdNotFoundException;

    /**
     * Find all the open {@link Offer}s published by the specified {@link User}.
     *
     * @param username The username of the user.
     * @return The list of the open offers published by the specified user.
     */
    List<Offer> findAllOpenByPublisherUsername(String username);

    /**
     * Find the {@link Offer} with the specified id.
     *
     * @param id The id of the offer.
     * @return The offer with the specified id.
     */
    Optional<Offer> findOpenById(String id);

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
    void update(Offer offer, String userId) throws UserIdNotFoundException, UserNotAuthorizedException;

    /**
     * Persist an offer into the database.
     *
     * @param offer The offer.
     */
    void save(Offer offer);
}
