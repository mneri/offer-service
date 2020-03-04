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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.mneri.offer.dto.OfferDto;
import me.mneri.offer.dto.UserDto;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * {@link Type} constants for mapping generic objects.
 *
 * @author mneri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Types {
    //@formatter:off
    public static final Type OFFER_DTO_LIST_TYPE = new TypeToken<List<OfferDto>>() {}.getType();
    public static final Type USER_DTO_LIST_TYPE  = new TypeToken<List<UserDto>>() {}.getType();
    //@formatter:on
}
