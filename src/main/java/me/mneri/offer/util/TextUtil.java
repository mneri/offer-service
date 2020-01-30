package me.mneri.offer.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * Utility class for strings and text.
 *
 * @author mneri
 */
@NoArgsConstructor(access = PRIVATE)
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
