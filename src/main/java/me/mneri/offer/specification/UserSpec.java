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

package me.mneri.offer.specification;

import me.mneri.offer.entity.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification definition for the {@link User} entity.
 *
 * @author mneri
 */
public interface UserSpec {
    /**
     * Return a {@link Specification} for the SQL predicate {@code user.id = 'value'}.
     *
     * @param value The value to match against the id.
     * @return The specification.
     */
    Specification<User> idIsEqualTo(String value);

    /**
     * Return a {@link Specification} for the SQL predicate {@code user.enabled = 1}.
     *
     * @return The specification.
     */
    Specification<User> isEnabled();

    /**
     * Return a {@link Specification} for the SQL predicate {@code user.username = 'value'}.
     *
     * @param value The value to match against username.
     * @return The specification.
     */
    Specification<User> usernameIsEqualTo(String value);
}
