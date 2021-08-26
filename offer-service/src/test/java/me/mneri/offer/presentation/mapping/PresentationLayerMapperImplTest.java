package me.mneri.offer.presentation.mapping;

import lombok.val;
import me.mneri.offer.business.pojo.OfferCreate;
import me.mneri.offer.business.pojo.OfferUpdate;
import me.mneri.offer.business.pojo.Paging;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.presentation.dto.OfferCreateDto;
import me.mneri.offer.presentation.dto.OfferDto;
import me.mneri.offer.presentation.dto.OfferUpdateDto;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.dto.UserDto;
import me.mneri.offer.test.answer.UnsupportedOperationAnswer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@ActiveProfiles("test")
class PresentationLayerMapperImplTest {
    private Clock startClock;

    private PresentationLayerMapperImpl presentationLayerMapperImpl;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        startClock = Mockito.mock(Clock.class, new UnsupportedOperationAnswer());
        Mockito.doReturn(Instant.parse("2020-01-01T08:00:00.00Z").toEpochMilli()).when(startClock).millis();

        presentationLayerMapperImpl = new PresentationLayerMapperImpl(new PresentationLayerMapperHelper(startClock));

        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void givenOffer_whenMapOfferToOfferDto_thenAllFieldsAreCorrectlyInitialised() {
        // Given
        val offer = newOfferMock();

        // When
        val actual = presentationLayerMapperImpl.mapOfferToOfferDto(offer);

        // Then
        Assertions.assertThat(actual.getCreateTime()).isEqualTo(offer.getCreateTime());
        Assertions.assertThat(actual.getCurrency()).isEqualTo(offer.getCurrency());
        Assertions.assertThat(actual.getDescription()).isEqualTo(offer.getDescription());
        Assertions.assertThat(actual.getId()).isEqualTo(offer.getId());
        Assertions.assertThat(actual.getPrice()).isEqualTo(offer.getPrice());
        Assertions.assertThat(actual.getTitle()).isEqualTo(offer.getTitle());
        Assertions.assertThat(actual.getTtl()).isEqualTo(offer.getTtl(startClock));

        // Fail as soon as a new field is added so we get a chance to update this test.
        Assertions.assertThat(OfferDto.class.getDeclaredFields().length).isEqualTo(7);
    }

    @Test
    void givenOfferCreateDto_whenMapOfferCreateDtoToOfferCreateIsCalled_thenAllFieldsAreCorrectlyInitialised() {
        // Given
        val createDto = newOfferCreateDtoMock();

        // When
        val actual = presentationLayerMapperImpl.mapOfferCreateDtoToOfferCreate(createDto);

        // Then
        Assertions.assertThat(actual.getCurrency()).isEqualTo(createDto.getCurrency());
        Assertions.assertThat(actual.getDescription()).isEqualTo(createDto.getDescription());
        Assertions.assertThat(actual.getPrice()).isEqualTo(createDto.getPrice());
        Assertions.assertThat(actual.getTitle()).isEqualTo(createDto.getTitle());
        Assertions.assertThat(actual.getTtl()).isEqualTo(createDto.getTtl());

        // Fail as soon as a new field is added so we get a chance to update this test.
        Assertions.assertThat(OfferCreate.class.getDeclaredFields().length).isEqualTo(5);
    }

    @Test
    void givenOfferUpdateDto_whenMapOfferUpdateDtoToOfferUpdateIsCalled_thenAllFieldsAreCorrectlyInitialised() {
        // Given
        val updateDto = newOfferUpdateDtoMock();

        // When
        val actual = presentationLayerMapperImpl.mapOfferUpdateDtoToOfferUpdate(updateDto);

        // Then
        Assertions.assertThat(actual.getCurrency()).isEqualTo(updateDto.getCurrency());
        Assertions.assertThat(actual.getDescription()).isEqualTo(updateDto.getDescription());
        Assertions.assertThat(actual.getPrice()).isEqualTo(updateDto.getPrice());
        Assertions.assertThat(actual.getTitle()).isEqualTo(updateDto.getTitle());
        Assertions.assertThat(actual.getTtl()).isEqualTo(updateDto.getTtl());

        // Fail as soon as a new field is added so we get a chance to update this test.
        Assertions.assertThat(OfferUpdate.class.getDeclaredFields().length).isEqualTo(5);
    }

    @Test
    void givenPagingDto_whenMapPagingDtoToPagingIsCalled_thenAllFieldsAreCorrectlyInitialised() {
        // Given
        val pagingDto = newPagingDtoMock();

        // When
        val actual = presentationLayerMapperImpl.mapPagingDtoToPaging(pagingDto);

        // Then
        Assertions.assertThat(actual.getPageNumber()).isEqualTo(pagingDto.getPageNumber());
        Assertions.assertThat(actual.getPageSize()).isEqualTo(pagingDto.getPageSize());

        // Fail as soon as a new field is added so we get a chance to update this test.
        Assertions.assertThat(Paging.class.getDeclaredFields().length).isEqualTo(2);
    }

    @Test
    void givenUser_whenMapUserToUserDtoIsCalled_thenAllFieldsAreCorrectlyInitialised() {
        // Given
        val user = newUserMock();

        // When
        val actual = presentationLayerMapperImpl.mapUserToUserDto(user);

        // Then
        Assertions.assertThat(actual.getId()).isEqualTo(user.getId());
        Assertions.assertThat(actual.getUsername()).isEqualTo(user.getUsername());

        // Fail as soon as a new field is added so we get a chance to update this test.
        Assertions.assertThat(UserDto.class.getDeclaredFields().length).isEqualTo(2);
    }

    private OfferCreateDto newOfferCreateDtoMock() {
        val offerCreateDto = Mockito.mock(OfferCreateDto.class, new UnsupportedOperationAnswer());

        Date startDate = new Date(startClock.millis());
        Date endDate = Date.from(Instant.parse("2020-01-31T22:00:00.00Z"));
        long ttl = endDate.getTime() - startDate.getTime();

        Mockito.doReturn("GBP").when(offerCreateDto).getCurrency();
        Mockito.doReturn("Buy one and get one free!").when(offerCreateDto).getDescription();
        Mockito.doReturn(new BigDecimal("0.00")).when(offerCreateDto).getPrice();
        Mockito.doReturn("Free Coffee").when(offerCreateDto).getTitle();
        Mockito.doReturn(ttl).when(offerCreateDto).getTtl();

        return offerCreateDto;
    }

    private Offer newOfferMock() {
        val offer = Mockito.mock(Offer.class, new UnsupportedOperationAnswer());

        Date startDate = new Date(startClock.millis());
        Date endDate = Date.from(Instant.parse("2020-01-31T22:00:00.00Z"));
        long ttl = endDate.getTime() - startDate.getTime();

        Mockito.doReturn(startDate).when(offer).getCreateTime();
        Mockito.doReturn("GBP").when(offer).getCurrency();
        Mockito.doReturn("Buy one and get one free!").when(offer).getDescription();
        Mockito.doReturn(endDate).when(offer).getEndTime();
        Mockito.doReturn(UUID.fromString("00000000-0000-0000-0000-000000000000")).when(offer).getId();
        Mockito.doReturn(new BigDecimal("0.00")).when(offer).getPrice();
        Mockito.doReturn("Free Coffee").when(offer).getTitle();
        Mockito.doReturn(ttl).when(offer).getTtl(Mockito.any(Clock.class));

        return offer;
    }

    private OfferUpdateDto newOfferUpdateDtoMock() {
        val offerCreateDto = Mockito.mock(OfferUpdateDto.class, new UnsupportedOperationAnswer());

        Date startDate = new Date(startClock.millis());
        Date endDate = Date.from(Instant.parse("2020-01-31T22:00:00.00Z"));
        long ttl = endDate.getTime() - startDate.getTime();

        Mockito.doReturn("GBP").when(offerCreateDto).getCurrency();
        Mockito.doReturn("Buy one and get one free!").when(offerCreateDto).getDescription();
        Mockito.doReturn(new BigDecimal("0.00")).when(offerCreateDto).getPrice();
        Mockito.doReturn("Free Coffee").when(offerCreateDto).getTitle();
        Mockito.doReturn(ttl).when(offerCreateDto).getTtl();

        return offerCreateDto;
    }

    private PagingDto newPagingDtoMock() {
        val pagingDto = Mockito.mock(PagingDto.class, new UnsupportedOperationAnswer());

        Mockito.doReturn(0).when(pagingDto).getPageNumber();
        Mockito.doReturn(64).when(pagingDto).getPageSize();

        return pagingDto;
    }

    private User newUserMock() {
        val user = Mockito.mock(User.class, new UnsupportedOperationAnswer());

        Mockito.doReturn(passwordEncoder.encode("password")).when(user).getEncodedPassword();
        Mockito.doReturn(UUID.fromString("11111111-1111-1111-1111-111111111111")).when(user).getId();
        Mockito.doReturn(true).when(user).isEnabled();
        Mockito.doReturn("user").when(user).getUsername();

        return user;
    }
}
