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

import me.mneri.offer.business.bean.OfferCreate;
import me.mneri.offer.business.bean.OfferUpdate;
import me.mneri.offer.business.bean.UserCreate;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import org.mapstruct.*;
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
    @Mapping(target = "enabled", ignore = true)
    User mapUserCreateToUser(UserCreate create);

    @Mapping(target = "cancelled", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    Offer mapOfferCreateToOffer(OfferCreate create, @Context @NonNull User publisher);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cancelled", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    void mergeOfferUpdateToOffer(@MappingTarget @NonNull Offer offer, OfferUpdate update);
}
