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
 * Thrown when the specified offer id couldn't be found.
 *
 * @author mneri
 */
public class OfferNotFoundException extends Throwable {
    private final String offerId;

    /**
     * Create a new instance.
     *
     * @param offerId The offer id.
     */
    public OfferNotFoundException(String offerId) {
        this.offerId = offerId;
    }

    @Override
    public String getMessage() {
        return String.format("Couldn't find the specified offer: offerId=%s", offerId);
    }

    /**
     * Return the id that has generated the exception.
     *
     * @return The id.
     */
    public String getOfferId() {
        return offerId;
    }
}
