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

package me.mneri.offer.mapping;

import lombok.SneakyThrows;
import me.mneri.offer.entity.Offer;
import org.modelmapper.Provider;

/**
 * Custom mapping provider capable of creating {@link Offer} instances.
 *
 * @author mneri
 */
class CustomProvider implements Provider<Object> {
    @Override
    @SneakyThrows
    public Object get(ProvisionRequest<Object> request) {
        Class<?> type = request.getRequestedType();

        // Since the Offer class doesn't have a default constructor we have to tell the mapper how to create a new
        // instance. If the type is Offer we use its builder to obtain a new instance, otherwise we return null.
        // Returning null from this method tells ModelMapper to guess the construction by itself.
        if (type.equals(Offer.class)) {
            return Offer.builder().build();
        } else {
            return null;
        }
    }
}
