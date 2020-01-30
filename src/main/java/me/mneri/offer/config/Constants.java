package me.mneri.offer.config;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constants {
    public static final int USERNAME_MAX_LENGTH = 24;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final String USERNAME_REGEXP = "[a-zA-Z0-9_]+";
}
