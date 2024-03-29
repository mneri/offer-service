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

import me.mneri.offer.business.exception.OfferIsCancelledException;
import me.mneri.offer.business.exception.OfferIsExpiredException;
import me.mneri.offer.business.exception.OfferNotFoundException;
import me.mneri.offer.business.pojo.Paging;
import me.mneri.offer.business.pojo.UserCreate;
import me.mneri.offer.data.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for accessing the user repository.
 *
 * @author Massimo Neri
 */
@PreAuthorize("denyAll()")
public interface UserService {
    /**
     * Find all enabled {@link User}s.
     *
     * @param paging The paging specification.
     * @return The list of all enabled users or an empty list if no enable user exist.
     */
    @PreAuthorize("permitAll()")
    List<User> findAllEnabled(Paging paging);

    /**
     * Find the user with the specified id in the repository.
     *
     * @param userId The id.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    @PreAuthorize("permitAll()")
    Optional<User> findById(UUID userId);

    /**
     * Find the user who published the offer identified by the specified id in the repository.
     *
     * @param offerId The offer id.
     * @return The user.
     */
    @PreAuthorize("permitAll()")
    User findByOfferId(UUID offerId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException;

    /**
     * Find the user with the specified username in the repository.
     *
     * @param username The username.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    @PreAuthorize("permitAll()")
    Optional<User> findByUsername(String username);

    /**
     * Persist a user into the database.
     *
     * @param create The data of the user to create.
     */
    @PreAuthorize("hasAuthority('user:write') or hasAuthority('user:write-any')")
    User save(UserCreate create);
}
