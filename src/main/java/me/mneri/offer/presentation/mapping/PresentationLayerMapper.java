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

import me.mneri.offer.business.bean.OfferCreate;
import me.mneri.offer.business.bean.OfferUpdate;
import me.mneri.offer.business.bean.Paging;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.presentation.dto.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the presentation layer classes.
 *
 * @author Massimo Neri
 */
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = PresentationLayerMapperHelper.class)
public interface PresentationLayerMapper {
    OfferCreate mapOfferCreateDtoToOfferCreate(OfferCreateDto dto);

    Iterable<OfferDto> mapOfferToOfferDto(Iterable<Offer> offers);

    @Mapping(target = "ttl", ignore = true)
    OfferDto mapOfferToOfferDto(Offer offer);

    OfferUpdate mapOfferUpdateDtoToOfferUpdate(OfferUpdateDto dto);

    Paging mapPagingDtoToPaging(PagingDto pagingDto);

    Iterable<UserDto> mapUserToUserDto(Iterable<User> user);

    UserDto mapUserToUserDto(User user);
}
