package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferReviewDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferSaveReviewCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class AddReviewUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetOfferDaoInterface test_getOfferDaoInterface;

    @Mock
    UpdateOfferSaveReviewCommandInterface test_updateOfferSaveReviewCommandInterface;

    @InjectMocks
    AddReviewUseCaseService test_addReviewUseCaseService;

    @Mock
    OfferGetDto test_offerGetDto;

    @Mock
    OfferReviewDto test_offerReviewDto;

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
    void get_invalid_offer_from_db_and_throw_NoSuchElementException() {
        UUID offerId = UUID.randomUUID();
        Mockito.when(test_offerReviewDto.offerId()).thenReturn(offerId);
        Mockito.when(test_getOfferDaoInterface.getOfferDto(offerId)).thenReturn(Optional.of(test_offerGetDto));
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto))
                    .thenThrow(MultipleValidationException.class);
            Assertions.assertThrows(MultipleValidationException.class,
                    () -> test_addReviewUseCaseService.addReview(test_offerReviewDto));

        }
    }

    @Test
    void get_valid_offer_from_db_with_status_not_Finished_and_throw_NoSuchElementException() {
        UUID offerId = UUID.randomUUID();
        test_offerGetDto = new OfferGetDto(
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
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        Mockito.when(test_offerReviewDto.offerId()).thenReturn(offerId);
        Mockito.when(test_getOfferDaoInterface.getOfferDto(offerId)).thenReturn(Optional.of(test_offerGetDto));
        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_addReviewUseCaseService.addReview(test_offerReviewDto));
    }

    @Test
    void get_valid_offer_from_db_with_status_Finished_and_succeed() {
        Double test_reviewValue = 3.0;
        UUID offerId = UUID.randomUUID();
        test_offerGetDto = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.FINISHED.toString(),
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
        Mockito.when(test_offerReviewDto.offerId()).thenReturn(offerId);
        Mockito.when(test_offerReviewDto.reviewValue()).thenReturn(test_reviewValue);
        Mockito.when(test_getOfferDaoInterface.getOfferDto(offerId)).thenReturn(Optional.of(test_offerGetDto));
        UUID reviewId = Assertions.assertDoesNotThrow(
                () -> test_addReviewUseCaseService.addReview(test_offerReviewDto));
        verify(test_updateOfferSaveReviewCommandInterface, times(1)).updateOfferAddReview(any());
        verify(test_updateOfferSaveReviewCommandInterface).updateOfferAddReview(test_offerSnapshotCaptor.capture());
        OfferSnapshot offerSnapshotCaptorValue = test_offerSnapshotCaptor.getValue();
        Assertions.assertEquals(test_reviewValue, offerSnapshotCaptorValue.reviewSnapshot().value());
        Assertions.assertNotNull(reviewId);
    }

}
