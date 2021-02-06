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

package me.mneri.offer.specification.impl;

import me.mneri.offer.entity.User;
import me.mneri.offer.entity.User_;
import me.mneri.offer.specification.UserSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link UserSpec} component.
 */
@Component
public class UserSpecImpl implements UserSpec {
    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<User> idIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(User_.id), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<User> isEnabled() {
        return (root, query, builder) -> builder.equal(root.get(User_.enabled), true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<User> usernameIsEqualTo(String value) {
        return (root, query, builder) -> builder.equal(root.get(User_.username), value);
    }
}
