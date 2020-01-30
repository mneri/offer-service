package me.mneri.offer.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TextUtil {
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
