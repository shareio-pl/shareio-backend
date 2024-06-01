package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferDereserveOfferCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;


class PeriodicOfferReservationTimeoutReservationTimeoutUseCaseServiceTests {


    AutoCloseable test_autoCloseable;

    @Mock
    GetAllOffersDaoInterface test_getAllOffersDaoInterface;

    @Mock
    UpdateOfferDereserveOfferCommandInterface test_updateOfferDereserveOfferCommandInterface;

    @InjectMocks
    PeriodicOfferReservationTimeoutReservationTimeoutUseCaseService test_periodicOfferReservationTimeoutReservationTimeoutUseCaseService;

    @Mock
    OfferGetDto test_offerGetDto1;

    @Mock
    OfferGetDto test_offerGetDto2;

    @Captor
    ArgumentCaptor<OfferSnapshot> test_offerSnapshotCaptor;


    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_no_valid_offers_from_db_and_dont_dereserve_any(){
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto1))
                    .thenThrow(MultipleValidationException.class);
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            test_periodicOfferReservationTimeoutReservationTimeoutUseCaseService.periodicOfferReservationTimeoutHandler();
            verify(test_updateOfferDereserveOfferCommandInterface, times(0)).dereserveOffer(any());        }
    }

    @Test
    void get_one_valid_non_reserved_offer_from_db_and_dont_dereserve_any(){
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
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
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            test_periodicOfferReservationTimeoutReservationTimeoutUseCaseService.periodicOfferReservationTimeoutHandler();
            verify(test_updateOfferDereserveOfferCommandInterface, times(0)).dereserveOffer(any());
        }
    }

    @Test
    void get_one_valid_reserved_offer_from_db_before_passing_time_and_dont_dereserve_any(){
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            test_offerGetDto1 = new OfferGetDto(
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    Status.RESERVED.toString(),
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
                    LocalDateTime.now().plus(Const.OFFER_RESERVATION_DURATION),
                    UUID.randomUUID(),
                    4.5,
                    LocalDateTime.now()
      );
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            test_periodicOfferReservationTimeoutReservationTimeoutUseCaseService.periodicOfferReservationTimeoutHandler();
            verify(test_updateOfferDereserveOfferCommandInterface, times(0)).dereserveOffer(any());
        }
    }

    @Test
    void get_one_valid_reserved_offer_from_db_and_dereserve_one(){
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            test_offerGetDto1 = new OfferGetDto(
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    Status.RESERVED.toString(),
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
                    LocalDateTime.now().minus(Const.OFFER_RESERVATION_DURATION.plus(Const.OFFER_RESERVATION_DURATION)),
                    UUID.randomUUID(),
                    4.5,
                    LocalDateTime.now()
          );
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            test_periodicOfferReservationTimeoutReservationTimeoutUseCaseService.periodicOfferReservationTimeoutHandler();
            verify(test_updateOfferDereserveOfferCommandInterface, times(1)).dereserveOffer(any());
            verify(test_updateOfferDereserveOfferCommandInterface).dereserveOffer(test_offerSnapshotCaptor.capture());
            OfferSnapshot captured_offerSnapshot = test_offerSnapshotCaptor.getValue();
            Assertions.assertNull(captured_offerSnapshot.receiver());
            Assertions.assertEquals(Status.CREATED, captured_offerSnapshot.status());
            Assertions.assertNull(captured_offerSnapshot.reservationDate());
        }
    }
}
