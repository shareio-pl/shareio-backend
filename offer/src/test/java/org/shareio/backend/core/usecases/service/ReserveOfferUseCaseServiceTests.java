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
import org.shareio.backend.core.usecases.port.dto.OfferReserveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferReserveOfferCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class ReserveOfferUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetUserProfileDaoInterface test_getUserProfileDaoInterface;
    @Mock
    GetOfferDaoInterface test_getOfferDaoInterface;
    @Mock
    UpdateOfferReserveOfferCommandInterface test_updateOfferReserveOfferCommandInterface;
    @Mock
    OfferGetDto test_offerGetDto;
    @Mock
    OfferReserveDto test_offerReserveDto;
    @Mock
    UserProfileGetDto test_userProfileGetDto;
    @InjectMocks
    ReserveOfferUseCaseService test_reserveOfferUseCaseService;
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
    String testEmail = "qwe@gmail.com";
    String testPwHash = "qwerty";

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
    void get_invalid_offer_and_throw_validation_exception() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            testOfferId = UUID.randomUUID();
            testPhotoId = UUID.randomUUID();
            testAddressId = UUID.randomUUID();
            testOwnerId = UUID.randomUUID();
            testOwnerPhotoId = UUID.randomUUID();
            testReceiverId = UUID.randomUUID();

            test_offerGetDto = new OfferGetDto(
                    testOfferId,
                    testDate,
                    Status.CREATED.toString(),
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
            test_offerReserveDto = new OfferReserveDto(testOfferId, testReceiverId);

            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto)).thenThrow(MultipleValidationException.class);
            when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
            Assertions.assertThrows(MultipleValidationException.class, () -> test_reserveOfferUseCaseService.reserveOffer(test_offerReserveDto));
        }
    }

    @Test
    void get_no_offer_for_id_and_throw_no_such_element_exception() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId = UUID.randomUUID();

        test_offerGetDto = new OfferGetDto(
                testOfferId,
                testDate,
                Status.CREATED.toString(),
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
        test_offerReserveDto = new OfferReserveDto(testOfferId, testReceiverId);

        UUID randomUUID = UUID.randomUUID();
        when(test_getOfferDaoInterface.getOfferDto(randomUUID)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> test_reserveOfferUseCaseService.reserveOffer(test_offerReserveDto));
    }

    @Test
    void get_offer_for_id_but_with_status_different_than_CREATED_and_throw_no_such_element_exception() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
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
                null,
                null,
                null,
                testReviewValue,
                testDate
        );
        test_offerReserveDto = new OfferReserveDto(testOfferId, testReceiverId);

        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
        Assertions.assertThrows(NoSuchElementException.class, () -> test_reserveOfferUseCaseService.reserveOffer(test_offerReserveDto));
    }

    @Test
    void get_offer_for_id_but_with_non_existing_user_and_throw_no_such_element_exception() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId = UUID.randomUUID();

        test_offerGetDto = new OfferGetDto(
                testOfferId,
                testDate,
                Status.CREATED.toString(),
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
        test_offerReserveDto = new OfferReserveDto(testOfferId, testReceiverId);

        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
        when(test_getUserProfileDaoInterface.getUserDto(testReceiverId)).thenThrow(NoSuchElementException.class);

        Assertions.assertThrows(NoSuchElementException.class, () -> test_reserveOfferUseCaseService.reserveOffer(test_offerReserveDto));
    }

    @Test
    void get_correct_request_and_reserve_it_correctly() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId = UUID.randomUUID();

        test_offerGetDto = new OfferGetDto(
                testOfferId,
                testDate,
                Status.CREATED.toString(),
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
        test_offerReserveDto = new OfferReserveDto(testOfferId, testReceiverId);
        test_userProfileGetDto = new UserProfileGetDto(
                testOwnerId,
                testEmail,
                testName,
                testSurname,
                LocalDate.now(),
                testPhotoId,
                testAddressId,
                testDate,
                testPwHash
        );

        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
        when(test_getUserProfileDaoInterface.getUserDto(testReceiverId)).thenReturn(Optional.of(test_userProfileGetDto));
        UUID offerResultId = Assertions.assertDoesNotThrow(() ->
                test_reserveOfferUseCaseService.reserveOffer(test_offerReserveDto));

        verify(test_updateOfferReserveOfferCommandInterface).reserveOffer(test_offerSnapshotCaptor.capture());
        OfferSnapshot reservedOffer = test_offerSnapshotCaptor.getValue();

        Assertions.assertEquals(Status.RESERVED, reservedOffer.status());
        Assertions.assertNotNull(reservedOffer.receiver());
        Assertions.assertNotNull(reservedOffer.reservationDate());
        Assertions.assertEquals(offerResultId, reservedOffer.offerId().getId());
    }
}
