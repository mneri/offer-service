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

package me.mneri.offer.configuration;

import me.mneri.offer.mapping.OfferMapper;
import me.mneri.offer.mapping.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application wide configuration for object mapping.
 *
 * @author mneri
 */
@Configuration
public class MappingConfiguration {
    @Bean
    public OfferMapper offerMapper() {
        return Mappers.getMapper(OfferMapper.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }
}
