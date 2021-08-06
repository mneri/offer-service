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

package me.mneri.offer.presentation.exception;

/**
 * Thrown when the specified page size is not legal.
 *
 * @author Massimo Neri
 */
public class IllegalPageSizeException extends Exception {
    private final int pageSize;

    /**
     * Create a new instance.
     *
     * @param pageSize The page size.
     */
    public IllegalPageSizeException(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String getMessage() {
        return String.format("The specified page size is not legal: pageSize=%d", getPageSize());
    }

    /**
     * Return the page size.
     *
     * @return The page size.
     */
    public int getPageSize() {
        return pageSize;
    }
}
