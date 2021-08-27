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

package me.mneri.offer.business.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.mneri.offer.data.entity.Authority;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.data.repository.AuthorityRepository;
import me.mneri.offer.data.repository.UserRepository;
import me.mneri.offer.data.specification.AuthoritySpec;
import me.mneri.offer.data.specification.UserSpec;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Simple implementation of the {@link UserDetailsService} interface.
 *
 * @author Massimo Neri
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
class UserDetailsServiceImpl implements UserDetailsService {
    @Mapper(componentModel = "spring",
            injectionStrategy = InjectionStrategy.CONSTRUCTOR)
    interface UserDetailsImplMapper {
        @Mapping(target = "authorities", ignore = true)
        @Mapping(target = "password", source = "encodedPassword")
        UserDetailsImpl mapUserToUserDetailsImpl(User user);

        @Mapping(target = "authority", source = "name")
        GrantedAuthorityImpl mapAuthorityToGrantedAuthorityImpl(Authority authority);

        List<GrantedAuthorityImpl> mapListOfAuthorityToListOfGrantedAuthorityImpl(List<Authority> authorities);
    }

    private final AuthorityRepository authorityRepository;

    private final AuthoritySpec authoritySpec;

    private final UserDetailsImplMapper mapper;

    private final UserRepository userRepository;

    private final UserSpec userSpec;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findOne(where(userSpec.usernameIsEqualTo(username)))
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<Authority> authorities = authorityRepository
                .findAll(where(authoritySpec.isEnabled())
                        .and(authoritySpec.ownerIdIsEqualTo(user.getId())));

        UserDetailsImpl userDetails = mapper.mapUserToUserDetailsImpl(user);
        userDetails.setAuthorities(mapper.mapListOfAuthorityToListOfGrantedAuthorityImpl(authorities));

        return userDetails;
    }
}
