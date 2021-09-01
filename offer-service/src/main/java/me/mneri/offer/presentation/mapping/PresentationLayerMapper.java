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

import me.mneri.offer.business.pojo.OfferCreate;
import me.mneri.offer.business.pojo.OfferUpdate;
import me.mneri.offer.business.pojo.Paging;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.presentation.dto.OfferCreateDto;
import me.mneri.offer.presentation.dto.OfferDto;
import me.mneri.offer.presentation.dto.OfferUpdateDto;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.dto.UserDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the presentation layer classes.
 *
 * @author Massimo Neri
 */
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = PresentationLayerMapperHelper.class)
public interface PresentationLayerMapper {
    /**
     * Map an instance of {@link OfferCreateDto} to a new instance of {@link OfferCreate}.
     *
     * @param dto The {@link OfferCreateDto} instance.
     * @return A new {@link OfferCreate} instance.
     */
    OfferCreate mapOfferCreateDtoToOfferCreate(OfferCreateDto dto);

    /**
     * Map the specified {@link Offer} instances to new instances of {@link OfferDto}.
     *
     * @param offers The {@link Offer} instances.
     * @return A {@link List} of newly instantiated {@link OfferDto}s.
     */
    List<OfferDto> mapOfferToOfferDto(Iterable<Offer> offers);

    /**
     * Map the specified {@link Offer} instance to a new instance of {@link OfferDto}.
     *
     * @param offer The {@link Offer} instance.
     * @return A new {@link OfferDto} instance.
     */
    @Mapping(target = "ttl", ignore = true) // Done by the helper
    OfferDto mapOfferToOfferDto(Offer offer);

    /**
     * Map an instance of {@link OfferUpdateDto} to a new instance of {@link OfferUpdate}.
     *
     * @param dto The {@link OfferUpdateDto} instance.
     * @return A new {@link OfferUpdate} instance.
     */
    OfferUpdate mapOfferUpdateDtoToOfferUpdate(OfferUpdateDto dto);

    /**
     * Map an instance of {@link PagingDto} to a new instance of {@link Paging}.
     *
     * @param dto The {@link PagingDto} instance.
     * @return A new {@link Paging} instance.
     */
    Paging mapPagingDtoToPaging(PagingDto dto);

    /**
     * Map the specified {@link User} instances to new instances of {@link UserDto}.
     *
     * @param users The {@link User} instances.
     * @return A {@link List} of newly instantiated {@link UserDto}s.
     */
    List<UserDto> mapUserToUserDto(Iterable<User> users);

    /**
     * Map the specified {@link User} instance to a new instance of {@link UserDto}.
     *
     * @param user The {@link User} instance.
     * @return A new {@link UserDto} instance.
     */
    UserDto mapUserToUserDto(User user);
}
