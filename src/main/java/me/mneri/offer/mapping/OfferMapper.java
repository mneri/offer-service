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

import me.mneri.offer.bean.OfferCreate;
import me.mneri.offer.bean.OfferUpdate;
import me.mneri.offer.dto.OfferCreateDto;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.OfferUpdateDto;
import me.mneri.offer.entity.Offer;
import org.mapstruct.*;

/**
 * Mapper for the {@link Offer} entity.
 *
 * @author mneri
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OfferMapper {
    OfferCreate createDtoToCreate(OfferCreateDto createDto);

    Iterable<OfferDto> entityToDto(Iterable<Offer> offers);

    OfferDto entityToDto(Offer offer);

    OfferCreateDto entityToCreateDto(Offer offer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mergeCreateDtoToEntity(@MappingTarget Offer offer, OfferCreateDto createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mergeCreateToEntity(@MappingTarget Offer offer, OfferCreate create);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mergeUpdateDtoToEntity(@MappingTarget Offer offer, OfferUpdateDto updateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mergeUpdateToEntity(@MappingTarget Offer offer, OfferUpdate update);

    OfferUpdate updateDtoToUpdate(OfferUpdateDto updateDto);
}
