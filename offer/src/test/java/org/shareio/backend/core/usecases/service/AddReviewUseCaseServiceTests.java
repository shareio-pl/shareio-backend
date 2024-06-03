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

    String testName = "John";
    String testSurname = "Doe";
    String testTitle = "Tytuł";
    String testCondition = "BROKEN";
    String testCategory = "OTHER";
    String testDescription = "Testowy opis, który zawiera przynajmniej 20 znaków";
    String testCountry = "Polska";
    String testRegion = "łódzkie";
    String testCity = "Łódź";
    String testStreet = "Piotrkowska";
    String testHouseNumber = "1";
    String testFlatNumber = "2";
    String testPostCode = "97-319";

    Double testLatitude = 1.5;
    Double testLongitude = 1.0;
    LocalDateTime testDate = LocalDateTime.now();
    Double testReviewValue = 3.5;

    UUID testOfferId = null;
    UUID testPhotoId = null;
    UUID testAddressId = null;
    UUID testOwnerId = null;
    UUID testOwnerPhotoId = null;
    UUID testReviewId = null;

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
        testOfferId = UUID.randomUUID();
        Mockito.when(test_offerReviewDto.offerId()).thenReturn(testOfferId);
        Mockito.when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));

        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto))
                    .thenThrow(MultipleValidationException.class);
            Assertions.assertThrows(MultipleValidationException.class,
                    () -> test_addReviewUseCaseService.addReview(test_offerReviewDto));

        }
    }

    @Test
    void get_valid_offer_from_db_with_status_not_Finished_and_throw_NoSuchElementException() {
        testOfferId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReviewId = UUID.randomUUID();

        test_offerGetDto = new OfferGetDto(
                testOfferId,
                testDate,
                Status.CANCELED.toString(),
                testAddressId,
                testCountry,
                testRegion,
                testCity,
                testStreet,
                testHouseNumber,
                testFlatNumber,
                testPostCode,
                testLatitude,
                testLongitude,
                testTitle,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId,
                testName,
                testSurname,
                testOwnerPhotoId,
                null,
                null,
                testReviewId,
                testReviewValue,
                testDate
        );
        Mockito.when(test_offerReviewDto.offerId()).thenReturn(testOfferId);
        Mockito.when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));

        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_addReviewUseCaseService.addReview(test_offerReviewDto));
    }

    @Test
    void get_valid_offer_from_db_with_status_Finished_and_succeed() {
        testOfferId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReviewId = UUID.randomUUID();

        test_offerGetDto = new OfferGetDto(
                testOfferId,
                testDate,
                Status.FINISHED.toString(),
                testAddressId,
                testCountry,
                testRegion,
                testCity,
                testStreet,
                testHouseNumber,
                testFlatNumber,
                testPostCode,
                testLatitude,
                testLongitude,
                testTitle,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId,
                testName,
                testSurname,
                testOwnerPhotoId,
                null, // TODO: if this is finished, this shouldn't be null
                null,
                testReviewId,
                testReviewValue,
                testDate
        );
        Mockito.when(test_offerReviewDto.offerId()).thenReturn(testOfferId);
        Mockito.when(test_offerReviewDto.reviewValue()).thenReturn(testReviewValue);
        Mockito.when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));

        UUID reviewId = Assertions.assertDoesNotThrow(
                () -> test_addReviewUseCaseService.addReview(test_offerReviewDto));
        verify(test_updateOfferSaveReviewCommandInterface, times(1)).updateOfferAddReview(any());
        verify(test_updateOfferSaveReviewCommandInterface).updateOfferAddReview(test_offerSnapshotCaptor.capture());

        OfferSnapshot offerSnapshotCaptorValue = test_offerSnapshotCaptor.getValue();
        Assertions.assertEquals(testReviewValue, offerSnapshotCaptorValue.reviewSnapshot().value());
        Assertions.assertNotNull(reviewId);
    }
}
