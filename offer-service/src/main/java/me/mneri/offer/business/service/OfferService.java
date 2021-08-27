/*
 * Copyright 2020 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mneri/offer-service.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.offer.business.service;

import me.mneri.offer.business.pojo.OfferCreate;
import me.mneri.offer.business.pojo.OfferUpdate;
import me.mneri.offer.business.pojo.Paging;
import me.mneri.offer.business.exception.OfferIsCancelledException;
import me.mneri.offer.business.exception.OfferIsExpiredException;
import me.mneri.offer.business.exception.OfferNotFoundException;
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for accessing the offer repository.
 *
 * @author Massimo Neri
 */
@PreAuthorize("denyAll()")
public interface OfferService {
    /**
     * Delete (cancel) an offer.
     *
     * @param offerId The id of the offer to delete.
     * @throws OfferIsCancelledException If the offer with the specified id was previously cancelled.
     * @throws OfferIsExpiredException   If the offer with the specified id has expired.
     * @throws OfferNotFoundException    If the offer with the specified id was not found in the repository.
     */
    void delete(UUID offerId) throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException;

    /**
     * Find all the open {@link Offer}s.
     *
     * @param paging The paging specification.
     * @return The list of the open offers.
     */
    Iterable<Offer> findAllOpen(Paging paging);

    /**
     * Find all the open {@link Offer}s published by the specified {@link User}.
     *
     * @param userId The id of the user.
     * @param paging The paging specification.
     * @return The list of the open offers published by the specified user.
     * @throws UserIsNotEnabledException If the specified user is not enabled.
     * @throws UserNotFoundException     If the user with the specified id was not found in the repository.
     */
    List<Offer> findAllOpenByPublisherId(UUID userId, Paging paging)
            throws UserIsNotEnabledException, UserNotFoundException;

    /**
     * Find the {@link Offer} with the specified id.
     *
     * @param id The id of the offer.
     * @return The offer with the specified id.
     */
    Optional<Offer> findById(UUID id);

    /**
     * Return {@code true} if the specified {@link Offer} is published by the specified {@link User}.
     *
     * @param offerId  The offer id.
     * @param username The user's username.
     * @return {@code true} if the specified offer is published by the specified user, {@code false} otherwise.
     */
    @SuppressWarnings("unused")
    boolean isPublishedByUser(UUID offerId, String username);

    /**
     * Update the specified {@link Offer} given the specified user id.
     * <p>
     * Only the publisher of a specific offer is granted the permission to update.
     *
     * @param offerId The id of the offer to update.
     * @param update  The data to update the offer with.
     * @throws OfferIsCancelledException If the offer with the specified id was previously cancelled.
     * @throws OfferIsExpiredException   If the offer with the specified id has expired.
     * @throws OfferNotFoundException    If the offer with the specified id was not found in the repository.
     */
    void update(UUID offerId, OfferUpdate update)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException;

    /**
     * Persist an offer into the database.
     *
     * @param create The to create the offer with.
     * @param userId The creator.
     * @return The offer.
     * @throws UserIsNotEnabledException If the user with the specified id is not enabled.
     * @throws UserNotFoundException     If a user with the specified id was not found in the repository.
     */
    Offer save(OfferCreate create, UUID userId) throws UserIsNotEnabledException, UserNotFoundException;
}
