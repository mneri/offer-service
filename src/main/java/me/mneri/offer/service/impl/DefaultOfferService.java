package me.mneri.offer.service.impl;

import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.UserIdNotFoundException;
import me.mneri.offer.exception.UserNotAuthorizedException;
import me.mneri.offer.repository.OfferRepository;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.OfferService;
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
public class DefaultOfferService implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    public List<Offer> findAllOpen() {
        return offerRepository.findAll(where(offerIsOpen()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Offer> findAllOpenByPublisherId(String id) throws UserIdNotFoundException {
        if (userRepository.count(where(userIsEnabled()).and(userIdIsEqualTo(id))) == 0) {
            throw new UserIdNotFoundException(id);
        }

        return offerRepository.findAll(where(offerIsOpen()).and(offerPublisherIdIsEqualTo(id)));
    }

    /**
     * {@inheritDoc}
     */
    public List<Offer> findAllOpenByPublisherUsername(String username) {
        return offerRepository.findAll(where(offerIsOpen()).and(offerPublisherUsernameIsEqualTo(username)));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Offer> findOpenById(String id) {
        return offerRepository.findOne(where(offerIsOpen()).and(offerIdIsEqualTo(id)));
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void save(Offer offer) {
        offerRepository.save(offer);
    }
}
