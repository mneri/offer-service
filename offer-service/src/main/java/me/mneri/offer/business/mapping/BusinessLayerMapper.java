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

package me.mneri.offer.business.mapping;

import me.mneri.offer.business.pojo.OfferCreate;
import me.mneri.offer.business.pojo.OfferUpdate;
import me.mneri.offer.business.pojo.UserCreate;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.lang.NonNull;

/**
 * Mapper for the business layer classes.
 *
 * @author Massimo Neri
 */
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = BusinessLayerMapperHelper.class)
public interface BusinessLayerMapper {
    /**
     * Map an instance of {@link UserCreate} to a new instance of {@link User}.
     * <p>
     * The following {@link User} fields are not mapped:
     * <ul>
     *     <li>{@code enabled}</li>
     *     <li>{@code authorities}</li>
     * </ul>
     *
     * @param create The {@link UserCreate} instance.
     * @return A new {@link User} instance.
     */
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User mapUserCreateToUser(UserCreate create);

    /**
     * Map an instance of {@link OfferCreate} to a new instance of {@link Offer}.
     * <p>
     * The following {@link Offer} fields are not mapped:
     * <ul>
     *     <li>{@code cancelled}</li>
     * </ul>
     * The value of the field {@code publisher} is determined from the {@code publisher} parameter.
     *
     * @param create    The {@link OfferCreate} instance.
     * @param publisher The publisher of the offer.
     * @return A new {@link Offer} instance.
     */
    @Mapping(target = "cancelled", ignore = true)
    @Mapping(target = "publisher", ignore = true) // Done by the helper
    Offer mapOfferCreateToOffer(OfferCreate create, @Context @NonNull User publisher);

    /**
     * Merge the state of the specified {@link OfferUpdate} object into the specified {@link Offer} instance.
     * <p>
     * The following {@link Offer} fields are not mapped:
     * <ul>
     *     <li>{@code cancelled}</li>
     *     <li>{@code publisher}</li>
     * </ul>
     *
     * @param offer  The {@link Offer} instance.
     * @param update The {@link OfferUpdate} instance.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cancelled", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    void mergeOfferUpdateToOffer(@MappingTarget @NonNull Offer offer, OfferUpdate update);
}
