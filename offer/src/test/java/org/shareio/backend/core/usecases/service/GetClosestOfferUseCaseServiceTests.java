package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.core.usecases.util.DistanceCalculator;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.Mockito.*;

class GetClosestOfferUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetAllOffersDaoInterface test_getAllOffersDaoInterface;

    @Mock
    GetLocationDaoInterface test_getLocationDaoInterface;

    @InjectMocks
    GetClosestOfferUseCaseService test_getClosestOfferUseCaseService;

    @Mock
    OfferGetDto test_offerGetDto1;

    @Mock
    OfferGetDto test_offerGetDto2;

    @Mock
    Location test_location;

    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_two_invalid_offers_and_throw_NoSuchElementException() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto1))
                    .thenThrow(MultipleValidationException.class);
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            Assertions.assertThrows(NoSuchElementException.class,
                    () -> test_getClosestOfferUseCaseService.getOfferResponseDto(test_location));

        }
    }

    @Test
    void get_one_valid_offer_not_CREATED_and_return_nothing() {
        test_offerGetDto1 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CANCELED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                51.78542745,
                19.43777623530606,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            UUID closestOfferId = Assertions.assertDoesNotThrow(
                    () -> test_getClosestOfferUseCaseService.getOfferResponseDto(test_location));

            Assertions.assertNull(closestOfferId);
        }
    }

    @Test
    void get_one_valid_offer_CREATED_and_return_its_UUID() {
        test_offerGetDto1 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                51.78542745,
                19.43777623530606,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            UUID closestOfferId = Assertions.assertDoesNotThrow(
                    () -> test_getClosestOfferUseCaseService.getOfferResponseDto(test_location));

            Assertions.assertEquals(test_offerGetDto1.offerId(), closestOfferId);
        }
    }

    @Test
    void get_two_valid_offers_CREATED_and_return_second_UUID() {
        test_offerGetDto1 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                5.0,
                3.0,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        test_offerGetDto2 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                0.1,
                0.1,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        test_location = new Location(0.0, 0.0);
        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        try (MockedStatic<DistanceCalculator> distanceCalculator = Mockito.mockStatic(DistanceCalculator.class)) {
            distanceCalculator.when(()->DistanceCalculator.calculateDistance(any(),any())).thenCallRealMethod();
            UUID closestOfferId = Assertions.assertDoesNotThrow(
                    () -> test_getClosestOfferUseCaseService.getOfferResponseDto(test_location));

            distanceCalculator.verify(
                    () -> DistanceCalculator.calculateDistance(any(), any()),
                    times(2)
            );
            Assertions.assertEquals(test_offerGetDto2.offerId(), closestOfferId);
        }
    }
}
