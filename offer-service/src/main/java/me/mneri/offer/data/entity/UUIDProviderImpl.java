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

package me.mneri.offer.data.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Default implementation of {@link UUIDProvider}.
 *
 * @author Massimo Neri
 */
@Component("uuidProvider")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UUIDProviderImpl implements UUIDProvider {
    /**
     * {@inheritDoc}
     */
    @Override
    public UUID createRandomUuid() {
        return UUID.randomUUID();
    }
}
