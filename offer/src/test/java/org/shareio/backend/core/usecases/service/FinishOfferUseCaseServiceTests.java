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
    UUID testReceiverId = null;

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
        testOfferId = UUID.randomUUID();
        when(test_offerEndDto.offerId()).thenReturn(testOfferId);
        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        try (MockedStatic<OfferValidator> utilities = mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto))
                    .thenThrow(MultipleValidationException.class);
            Assertions.assertThrows(MultipleValidationException.class,
                    () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
                    );
        }
    }

    @Test
    void get_valid_offer_with_status_not_RESERVED_and_throw_NoSuchElementException() {
        testOfferId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

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
                null,
                testReviewValue,
                testDate
        );
        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        when(test_offerEndDto.offerId()).thenReturn(testOfferId);

        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
        );
    }

    @Test
    void get_valid_offer_with_status_RESERVED_and_wrong_userId_and_throw_NoSuchElementException() {
        testOfferId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId = UUID.randomUUID();

        test_offerGetDto = new OfferGetDto(
                testOfferId,
                testDate,
                Status.RESERVED.toString(),
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
                testReceiverId,
                testDate,
                null,
                testReviewValue,
                testDate
        );
        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        when(test_offerEndDto.offerId()).thenReturn(testOfferId);
        when(test_offerEndDto.userId()).thenReturn(UUID.randomUUID());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
        );
    }

    @Test
    void get_valid_offer_with_status_RESERVED_and_succeed() {
        testOfferId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId = UUID.randomUUID();

        test_offerGetDto = new OfferGetDto(
                testOfferId,
                testDate,
                Status.RESERVED.toString(),
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
                testReceiverId,
                testDate,
                null,
                testReviewValue,
                testDate
        );
        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(
                test_offerGetDto
        ));
        when(test_offerEndDto.offerId()).thenReturn(testOfferId);
        when(test_offerEndDto.userId()).thenReturn(testReceiverId);
        UUID finalOfferId = Assertions.assertDoesNotThrow(
                () -> test_finishOfferUseCaseService.finishOffer(test_offerEndDto)
        );
        verify(test_updateOfferFinishOfferCommandInterface, times(1)).finishOffer(any());
        verify(test_updateOfferFinishOfferCommandInterface).finishOffer(test_offerSnapshotCaptor.capture());
        OfferSnapshot test_offerSnapshotCaptorValue = test_offerSnapshotCaptor.getValue();
        Assertions.assertEquals(Status.FINISHED, test_offerSnapshotCaptorValue.status());
        Assertions.assertNotNull(finalOfferId);
    }
}
