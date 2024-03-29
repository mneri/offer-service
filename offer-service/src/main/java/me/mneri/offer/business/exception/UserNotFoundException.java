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

package me.mneri.offer.business.exception;

import java.util.UUID;

/**
 * Thrown when the specified user couldn't be found.
 *
 * @author Massimo Neri
 */
public class UserNotFoundException extends Exception {
    private final UUID userId;

    /**
     * Create a new instance.
     *
     * @param offerId The offer id.
     */
    public UserNotFoundException(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return String.format("Couldn't find the specified user: userId=%s", userId.toString());
    }

    /**
     * Return the id that has generated the exception.
     *
     * @return The id.
     */
    public UUID getUserId() {
        return userId;
    }
}
