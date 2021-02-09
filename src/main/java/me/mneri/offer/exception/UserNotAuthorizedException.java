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

package me.mneri.offer.exception;

/**
 * Thrown when a user is not authorized to read or write the specified resource.
 *
 * @author mneri
 */
public class UserNotAuthorizedException extends Exception {
    private final String userId;

    /**
     * Create a new instance.
     *
     * @param userId The user id.
     */
    public UserNotAuthorizedException(String userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return String.format("The user is not authorized: userId=%s", userId);
    }

    /**
     * Return the id of the user that has generated the exception.
     *
     * @return The id of the user.
     */
    public String getUserId() {
        return userId;
    }
}
