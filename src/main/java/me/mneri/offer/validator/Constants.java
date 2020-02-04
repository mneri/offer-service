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
