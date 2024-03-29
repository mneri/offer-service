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
 * Thrown when the specified offer is expired.
 *
 * @author Massimo Neri
 */
public class OfferIsExpiredException extends Exception {
    private final UUID offerId;

    /**
     * Create a new instance.
     *
     * @param offerId The offer id.
     */
    public OfferIsExpiredException(UUID offerId) {
        this.offerId = offerId;
    }

    @Override
    public String getMessage() {
        return String.format("The offer is expired: offerId=%s", offerId.toString());
    }

    /**
     * Return the id of the offer that has generated the exception.
     *
     * @return The id of the offer.
     */
    public UUID getOfferId() {
        return offerId;
    }
}
