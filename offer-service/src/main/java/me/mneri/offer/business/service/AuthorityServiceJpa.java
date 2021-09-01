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

package me.mneri.offer.business.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.mneri.offer.data.entity.Authority;
import me.mneri.offer.data.repository.AuthorityRepository;
import me.mneri.offer.data.specification.AuthoritySpec;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Service for accessing the authority repository.
 *
 * @author Massimo Neri
 */
@Log4j2
@Primary
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
class AuthorityServiceJpa implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public List<Authority> findAllByOwnerId(UUID userId) {
        return authorityRepository
                .findAll(where(AuthoritySpec.isEnabled()).and(AuthoritySpec.ownerIdIsEqualTo(userId)));
    }
}
