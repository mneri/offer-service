package me.mneri.offer.data.entity;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.util.Date;
import java.util.UUID;

public class EntityFactoryImplTest {
    private Clock clock;

    private EntityFactoryImpl entityFactory;

    private UUID uuid;

    @BeforeEach
    private void beforeEach() {
        clock = Mockito.mock(Clock.class);

        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

        UUIDProvider uuidProvider = Mockito.mock(UUIDProvider.class);
        Mockito.doReturn(uuid).when(uuidProvider).createRandomUuid();

        entityFactory = new EntityFactoryImpl(clock, uuidProvider);
    }

    @Test
    public void givenUuid_whenCreateOfferIsCalled_thenIdFieldIsSetCorrectly() {
        // Given

        // When
        val offer = entityFactory.createOffer();

        // Then
        Assertions.assertThat(offer.getId()).isEqualTo(uuid);
    }

    @Test
    public void givenClock_whenCreateOfferIsCalled_thenCreateDateFieldIsSetCorrectly() {
        // Given
        Mockito.doReturn(0L).when(clock).millis();

        // When
        val offer = entityFactory.createOffer();

        // Then
        Assertions.assertThat(offer.getCreateTime()).isEqualTo(new Date(clock.millis()));
    }

    @Test
    public void givenUuid_whenCreateUserIsCalled_thenIdFieldIsSetCorrectly() {
        // Given

        // When
        val user = entityFactory.createUser();

        // Then
        Assertions.assertThat(user.getId()).isEqualTo(uuid);
    }

    @Test
    public void givenNothing_whenCreateUserIsCalled_thenUserIsEnabled() {
        // Given

        // When
        val user = entityFactory.createUser();

        // Then
        Assertions.assertThat(user.isEnabled()).isTrue();
    }
}
