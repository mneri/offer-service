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

package me.mneri.offer.service.impl;

import lombok.extern.log4j.Log4j2;
import me.mneri.offer.entity.User;
import me.mneri.offer.repository.UserRepository;
import me.mneri.offer.service.UserService;
import me.mneri.offer.specification.UserSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * Service for accessing the user repository.
 *
 * @author mneri
 */
@Log4j2
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSpec userSpec;

    /**
     * {@inheritDoc}
     */
    public List<User> findAllEnabled() {
        return userRepository.findAll(where(userSpec.isEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<User> findEnabledById(String id) {
        return userRepository.findOne(where(userSpec.isEnabled()).and(userSpec.idIsEqualTo(id)));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<User> findEnabledByUsername(String username) {
        return userRepository.findOne(where(userSpec.isEnabled()).and(userSpec.usernameIsEqualTo(username)));
    }

    /**
     * {@inheritDoc}
     */
    public void save(User user) {
        userRepository.save(user);
        log.debug("User created; userId: {}", user.getId());
    }
}
