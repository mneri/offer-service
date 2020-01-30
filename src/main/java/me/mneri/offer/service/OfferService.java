package me.mneri.offer.service;

import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static me.mneri.offer.specification.OfferSpecification.*;
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

    /**
     * Return whether or not an open {@link Offer} with the specified id exists in the database.
     *
     * @param id The offer's id.
     * @return {@code true} if such an offer exists, false otherwise.
     */
    public boolean existsOpenById(String id) {
        return offerRepository.count(where(isOpen()).and(idIsEqualTo(id))) == 1;
    }

    /**
     * Find all the open {@link Offer}s.
     *
     * @return The list of the open offers.
     */
    public List<Offer> findAllOpen() {
        return offerRepository.findAll(where(isOpen()));
    }

    /**
     * Find all the open {@link Offer}s published by the specified {@link User}.
     *
     * @param id The id of the user.
     * @return The list of the open offers published by the specified user.
     */
    public List<Offer> findAllOpenByPublisherId(String id) {
        return offerRepository.findAll(where(isOpen()).and(publisherIdIsEqualTo(id)));
    }

    /**
     * Find all the open {@link Offer}s published by the specified {@link User}.
     *
     * @param username The username of the user.
     * @return The list of the open offers published by the specified user.
     */
    public List<Offer> findAllOpenByPublisherUsername(String username) {
        return offerRepository.findAll(where(isOpen()).and(publisherUsernameIsEqualTo(username)));
    }

    /**
     * Find the {@link Offer} with the specified id.
     *
     * @param id The id of the offer.
     * @return The offer with the specified id.
     */
    public Optional<Offer> findOpenById(String id) {
        return offerRepository.findOne(where(isOpen()).and(idIsEqualTo(id)));
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
