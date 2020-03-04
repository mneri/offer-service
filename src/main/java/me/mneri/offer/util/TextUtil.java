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

package me.mneri.offer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for strings and text.
 *
 * @author mneri
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextUtil {
    /**
     * Return {@code true} if the string is {@code null} or empty, {@code false} otherwise.
     *
     * @param string The string to test.
     * @return {@code true} if the string is {@code null} or empty, {@code false} otherwise.
     */
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
