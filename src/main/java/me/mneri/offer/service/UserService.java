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

import me.mneri.offer.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Service for accessing the user repository.
 *
 * @author mneri
 */
public interface UserService {
    /**
     * Find all enabled {@link User}s.
     *
     * @return The list of all enabled users.
     */
    List<User> findAllEnabled();

    /**
     * Find the user with the specified id in the repository.
     *
     * @param id The id.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    Optional<User> findEnabledById(String id);

    /**
     * Find the user with the specified username in the repository.
     *
     * @param username The username.
     * @return If the user is present return an {@link Optional} of the user; otherwise return an empty
     * {@link Optional}.
     */
    Optional<User> findEnabledByUsername(String username);

    /**
     * Persist a user into the database.
     *
     * @param user The user.
     */
    void save(User user);
}
