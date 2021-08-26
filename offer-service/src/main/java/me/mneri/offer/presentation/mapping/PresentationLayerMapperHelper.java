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

package me.mneri.offer.presentation.mapping;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.presentation.dto.OfferDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * Helper for the presentation layer classes mapping.
 *
 * @author Massimo Neri
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED, onConstructor_ = @Autowired)
class PresentationLayerMapperHelper {
    private final Clock clock;

    /**
     * Called after a {@link Offer} to {@link OfferDto} mapping.
     *
     * @param dto   The offer DTO.
     * @param offer The offer.
     */
    @AfterMapping
    void afterMapOfferToOfferDto(@MappingTarget OfferDto dto, Offer offer) {
        if (offer != null) {
            dto.setTtl(offer.getTtl(clock));
        }
    }
}
