package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferEndDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferFinishOfferCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


class FinishOfferUseCaseServiceTests {

    AutoCloseable test_autoCloseable;

    @Mock
    GetOfferDaoInterface test_getOfferDaoInterface;

    @Mock
    UpdateOfferFinishOfferCommandInterface test_updateOfferFinishOfferCommandInterface;

    @InjectMocks
    FinishOfferUseCaseService test_finishOfferUseCaseService;

    @Captor
    ArgumentCaptor<OfferSnapshot> test_offerSnapshotCaptor;

    @Mock
    OfferGetDto test_offerGetDto;

    @Mock
    OfferEndDto test_offerEndDto;

    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_invalid_offer_and_throw_MultipleValidationException() {
        UUID offerId = UUID.randomUUID();
        when(test_offerEndDto.offerId()).thenReturn(offerId);
        when(test_getOfferDaoInterface.getOfferDto(offerId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto))
                    .thenThrow(MultipleValidationException.class);
            Assertions.assertThrows(MultipleValidationException.class,
                    () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
                    );
        }
    }

    @Test
    void get_valid_offer_with_status_not_RESERVED_and_throw_NoSuchElementException() {
        UUID offerId = UUID.randomUUID();
        when(test_offerEndDto.offerId()).thenReturn(offerId);
        test_offerGetDto = new OfferGetDto(
                offerId,
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
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        when(test_getOfferDaoInterface.getOfferDto(offerId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
        );
    }

    @Test
    void get_valid_offer_with_status_RESERVED_and_wrong_userId_and_throw_NoSuchElementException() {
        UUID offerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(test_offerEndDto.offerId()).thenReturn(offerId);
        when(test_offerEndDto.userId()).thenReturn(userId);
        test_offerGetDto = new OfferGetDto(
                offerId,
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
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        when(test_getOfferDaoInterface.getOfferDto(offerId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
        );
    }

    @Test
    void get_valid_offer_with_status_RESERVED_and_succeed() {
        UUID offerId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(test_offerEndDto.offerId()).thenReturn(offerId);
        when(test_offerEndDto.userId()).thenReturn(userId);
        test_offerGetDto = new OfferGetDto(
                offerId,
                LocalDateTime.now(),
                Status.CREATED.toString(),
                userId,
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
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        when(test_getOfferDaoInterface.getOfferDto(offerId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        Assertions.assertDoesNotThrow(
                () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
        );
        verify(test_updateOfferFinishOfferCommandInterface, times(1)).finishOffer(any());
        verify(test_updateOfferFinishOfferCommandInterface).finishOffer(test_offerSnapshotCaptor.capture());
        OfferSnapshot test_offerSnapshotCaptorValue = test_offerSnapshotCaptor.getValue();
        Assertions.assertEquals(Status.FINISHED, test_offerSnapshotCaptorValue.status());
    }
}
