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

package me.mneri.offer.service;

import me.mneri.offer.bean.OfferCreate;
import me.mneri.offer.bean.OfferUpdate;
import me.mneri.offer.entity.Offer;
import me.mneri.offer.entity.User;
import me.mneri.offer.exception.*;

import java.util.List;
import java.util.Optional;

/**
 * Service for accessing the offer repository.
 *
 * @author mneri
 */
public interface OfferService {
    /**
     * Delete (cancel) an offer.
     *
     * @param offerId The id of the offer to delete.
     * @param userId  The id of the user.
     * @throws OfferIsCancelledException  If the offer with the specified id was previously cancelled.
     * @throws OfferIsExpiredException    If the offer with the specified id has expired.
     * @throws OfferNotFoundException     If the offer with the specified id was not found in the repository.
     * @throws UserIsNotEnabledException  If the user with the specified id is not enabled.
     * @throws UserNotFoundException      If a user with the specified id was not found in the repository.
     * @throws UserNotAuthorizedException If the specified user id doesn't belong to the publisher of the offer.
     */
    void delete(String offerId, String userId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotAuthorizedException, UserNotFoundException;

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
     * @throws UserIsNotEnabledException If the specified user is not enabled.
     * @throws UserNotFoundException     If the user with the specified id was not found in the repository.
     */
    List<Offer> findAllOpenByPublisherId(String id) throws UserIsNotEnabledException, UserNotFoundException;

    /**
     * Find the {@link Offer} with the specified id.
     *
     * @param id The id of the offer.
     * @return The offer with the specified id.
     */
    Optional<Offer> findById(String id);

    /**
     * Update the specified {@link Offer} given the specified user id.
     * <p>
     * Only the publisher of a specific offer is granted the permission to update.
     *
     * @param offerId The id of the offer to update.
     * @param update  The data to update the offer with.
     * @param userId  The user id of the modifier.
     * @throws OfferIsCancelledException  If the offer with the specified id was previously cancelled.
     * @throws OfferIsExpiredException    If the offer with the specified id has expired.
     * @throws OfferNotFoundException     If the offer with the specified id was not found in the repository.
     * @throws UserIsNotEnabledException  If the user with the specified id is not enabled.
     * @throws UserNotFoundException      If a user with the specified id was not found in the repository.
     * @throws UserNotAuthorizedException If the specified user id doesn't belong to the publisher of the offer.
     */
    void update(String offerId, OfferUpdate update, String userId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotAuthorizedException, UserNotFoundException;

    /**
     * Persist an offer into the database.
     *
     * @param create The to create the offer with.
     * @param userId The creator.
     * @return The offer.
     * @throws UserIsNotEnabledException If the user with the specified id is not enabled.
     * @throws UserNotFoundException     If a user with the specified id was not found in the repository.
     */
    Offer save(OfferCreate create, String userId) throws UserIsNotEnabledException, UserNotFoundException;
}
