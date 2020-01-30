package me.mneri.offer.validator;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class Constants {
    public static final int OFFER_DESCRIPTION_MAX_LENGTH = 8192;
    public static final int OFFER_DESCRIPTION_MIN_LENGTH = 1;
    public static final int OFFER_TITLE_MAX_LENGTH = 256;
    public static final int OFFER_TITLE_MIN_LENGTH = 1;

    public static final int USERNAME_MAX_LENGTH = 24;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final String USERNAME_REGEXP = "[a-zA-Z0-9_]+";
}
