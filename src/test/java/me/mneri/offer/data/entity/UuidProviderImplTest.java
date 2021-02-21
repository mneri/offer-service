package me.mneri.offer.data.entity;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UuidProviderImplTest {
    private UuidProviderImpl uuidProvider;

    @BeforeEach
    public void beforeEach() {
        uuidProvider = new UuidProviderImpl();
    }

    @Test
    public void givenNothing_whenRandomIsCalled_thenANonNullValueIsReturned() {
        // Given
        // Nothing...

        // When
        val actual = uuidProvider.random();

        // Then
        Assertions.assertThat(actual).isNotNull();
    }
}
