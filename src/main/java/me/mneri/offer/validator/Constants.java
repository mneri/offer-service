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

package me.mneri.offer.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Contains all the constants for validation in a single centralized point.
 *
 * @author mneri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    /**
     * The maximum length for a description field.
     */
    public static final int DESCRIPTION_MAX_LENGTH = 8192;

    /**
     * The minimum length for a description field.
     */
    public static final int DESCRIPTION_MIN_LENGTH = 1;

    /**
     * The maximum length for a title field.
     */
    public static final int TITLE_MAX_LENGTH = 256;

    /**
     * The minimum length for a description field.
     */
    public static final int TITLE_MIN_LENGTH = 1;

    /**
     * The maximum length for a username field.
     */
    public static final int USERNAME_MAX_LENGTH = 24;

    /**
     * The minimum length for a username field.
     */
    public static final int USERNAME_MIN_LENGTH = 3;

    /**
     * The regular expression defining the format of a username field.
     */
    public static final String USERNAME_REGEXP = "[a-zA-Z0-9_]+";
}
