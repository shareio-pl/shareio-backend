package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.GetLocationUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GetOfferUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetUserProfileDaoInterface test_getUserProfileDaoInterface;
    @Mock
    GetLocationUseCaseInterface test_getLocationUseCaseInterface;
    @Mock
    GetOfferDaoInterface test_getOfferDaoInterface;
    @Mock
    OfferGetDto test_offerGetDto;
    @Mock
    UserProfileGetDto test_userProfileGetDto;
    @Mock LocationResponseDto test_locationResponseDto;
    @InjectMocks
    GetOfferUseCaseService test_getOfferUseCaseService;

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
    String testDistance = "0"; // because the address is the same, it should return 0
    String testEmail = "qwe@gmail.com";
    String testPwHash = "qwerty";

    Double testLatitude = 1.5;
    Double testLongitude = 1.0;
    LocalDateTime testDate = LocalDateTime.now();
    Double testReviewValue = 3.5;
    Integer testReviewCount = 5;
    Double testAverageReview = 3.5;

    UUID testOfferId = null;
    UUID testPhotoId = null;
    UUID testAddressId = null;
    UUID testOwnerId = null;
    UUID testOwnerPhotoId = null;

    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_invalid_offer_with_matching_id_and_throw_validation_exception() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            testOfferId = UUID.randomUUID();
            testPhotoId = UUID.randomUUID();
            testAddressId = UUID.randomUUID();
            testOwnerId = UUID.randomUUID();
            testOwnerPhotoId = UUID.randomUUID();

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

            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto)).thenThrow(MultipleValidationException.class);
            when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
            Assertions.assertThrows(MultipleValidationException.class, () ->
                    test_getOfferUseCaseService.getOfferResponseDto(testOfferId, testOwnerId, testReviewCount, testAverageReview));
        }
    }

    @Test
    void get_no_offers_for_id_and_throw_no_such_element_exception() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

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

        UUID randomUUID = UUID.randomUUID();
        when(test_getOfferDaoInterface.getOfferDto(randomUUID)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> test_getOfferUseCaseService.getOfferResponseDto(randomUUID, testOwnerId, testReviewCount, testAverageReview));
    }

    @Test
    void get_offer_for_id_but_with_non_existing_user_and_throw_no_such_element_exception() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

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

        UUID randomUUID = UUID.randomUUID();
        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
        when(test_getUserProfileDaoInterface.getUserDto(randomUUID)).thenThrow(NoSuchElementException.class);

        Assertions.assertThrows(NoSuchElementException.class, () -> test_getOfferUseCaseService.getOfferResponseDto(testOwnerId, randomUUID, testReviewCount, testAverageReview));
    }

    @Test
    void get_offer_for_id_with_status_CANCELED_and_throw_no_such_element_exception() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
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

        when(test_getOfferDaoInterface.getOfferDto(testOwnerId)).thenReturn(Optional.of(test_offerGetDto));
        Assertions.assertThrows(NoSuchElementException.class, () -> test_getOfferUseCaseService.getOfferResponseDto(testOfferId, testOfferId, testReviewCount, testAverageReview));
    }

    @Test
    void get_offer_for_id_with_valid_user_but_location_fetch_fails_and_still_return_response_dto() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

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


        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
        when(test_getUserProfileDaoInterface.getUserDto(testOwnerId)).thenThrow(NoSuchElementException.class);
        try {
            when(test_getLocationUseCaseInterface.getLocationResponseDto(testAddressId)).thenThrow(NoSuchElementException.class);
        } catch (MultipleValidationException e) {
            Assertions.fail();
        }

        OfferResponseDto responseDto = null;
        try {
            responseDto = test_getOfferUseCaseService.getOfferResponseDto(testOfferId, testOwnerId, testReviewCount, testAverageReview);
        } catch (MultipleValidationException e) {
            Assertions.fail();
        }
        Assertions.assertNotNull(responseDto);
    }

    @Test
    void get_offer_with_matching_id_and_return_correct_response_dto() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

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
                null,
                null
        );

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

        test_locationResponseDto = new LocationResponseDto(testLatitude, testLongitude);

        OfferResponseDto expectedResponseDto = new OfferResponseDto(testOfferId,
                testDate,
                Status.CREATED.toString(),
                testCity,
                testStreet,
                testHouseNumber,
                testDistance,
                testLatitude,
                testLongitude,
                testTitle,
                Condition.BROKEN.polishName(),
                Category.OTHER.polishName(),
                testDescription,
                testPhotoId,
                testOwnerId,
                testName,
                testSurname,
                testOwnerPhotoId,
                testAverageReview,
                testReviewCount,
                null,
                null,
                null,
                null
        );

        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
        when(test_getUserProfileDaoInterface.getUserDto(testOwnerId)).thenReturn(Optional.of(test_userProfileGetDto));
        try {
            when(test_getLocationUseCaseInterface.getLocationResponseDto(testAddressId)).thenReturn(test_locationResponseDto);
            Assertions.assertDoesNotThrow(() -> test_getOfferUseCaseService.getOfferResponseDto(testOfferId, testOwnerId, testReviewCount, testAverageReview));
            Assertions.assertEquals(expectedResponseDto, test_getOfferUseCaseService.getOfferResponseDto(testOfferId, testOwnerId, testReviewCount, testAverageReview));
        } catch (MultipleValidationException e) {
            Assertions.fail();
        }
    }
}
