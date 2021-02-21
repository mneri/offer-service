package me.mneri.offer.presentation.exception;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IllegalPageSizeExceptionTest {
    @Test
    void givenPageSize_whenAllArgsConstructorIsCalled_thenAllFieldsAreCorrectlyInitialised() {
        // Given
        val pageSize = 25;

        // When
        val actual = new IllegalPageSizeException(pageSize);

        // Then
        Assertions.assertEquals(pageSize, actual.getPageSize());
    }
}
