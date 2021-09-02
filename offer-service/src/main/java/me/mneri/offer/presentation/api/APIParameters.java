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

package me.mneri.offer.presentation.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Collection of constants regulating API parameters.
 *
 * @author Massimo Neri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class APIParameters {
    public static final String PARAM_PAGE_NUMBER = "page.number";

    public static final int PARAM_PAGE_NUMBER_DEFAULT = 0;

    public static final String PARAM_PAGE_SIZE = "page.size";

    public static final int PARAM_PAGE_SIZE_DEFAULT = 24;

    public static final int PARAM_PAGE_SIZE_MAX = 128;

    public static final int PARAM_PAGE_SIZE_MIN = 24;
}
