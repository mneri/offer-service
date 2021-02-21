package me.mneri.offer.presentation.dto;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PagingDtoTest {
    @Test
    void givenPageNumberAndPageSize_whenAllArgsConstructorIsCalled_thenAllFieldsAreCorrectlyInitialised() {
        // Given
        val pageNumber = 5;
        val pageSize = 25;

        // When
        val actual = new PagingDto(pageNumber, pageSize);

        // Then
        Assertions.assertThat(actual.getPageNumber()).isEqualTo(pageNumber);
        Assertions.assertThat(actual.getPageSize()).isEqualTo(pageSize);
    }
}
