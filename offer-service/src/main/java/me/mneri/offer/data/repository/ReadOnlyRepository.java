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

package me.mneri.offer.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
@SuppressWarnings("unused")
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
    /**
     * Returns the number of entities available.
     *
     * @return The number of entities.
     */
    long count();

    /**
     * Returns all instances of the type.
     *
     * @return All entities.
     */
    List<T> findAll();

    /**
     * Returns all instances of the type sorted by the specified {@link Sort}.
     *
     * @param sort The sort.
     * @return All entities.
     */
    List<T> findAll(Sort sort);

    /**
     * Returns all instances of the type paged following the specified {@link Pageable}.
     *
     * @param pageable The pageable.
     * @return
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Retrieves an entity by its id.
     *
     * @param id Must not be {@literal null}.
     * @return The entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    Optional<T> findById(ID id);
}
