package me.mneri.offer.util;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextUtilTest {
    @Test
    void givenEmptyString_whenIsEmptyIsInvoked_thenTrueIsReturned() {
        // When
        boolean empty = TextUtil.isEmpty("");

        // Then
        assertTrue(empty);
    }

    @Test
    void giveNonEmptyString_whenIsEmptyIsInvoked_thenFalseIsReturned() {
        // Given
        val string = "Hello, world!";

        // When
        boolean empty = TextUtil.isEmpty(string);

        // Then
        assertFalse(empty);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void givenNull_whenIsEmptyIsInvoked_thenTrueIsReturned() {
        // When
        boolean empty = TextUtil.isEmpty(null);

        // Then
        assertTrue(empty);
    }
}
