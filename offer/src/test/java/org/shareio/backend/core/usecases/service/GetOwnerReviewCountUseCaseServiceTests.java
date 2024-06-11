package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GetOwnerReviewCountUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetAllOffersDaoInterface test_getAllOffersDaoInterface;
    @Mock
    OfferGetDto test_offerGetDto1;
    @Mock
    OfferGetDto test_offerGetDto2;
    @InjectMocks
    GetOwnerReviewCountUseCaseService test_getOwnerReviewCountUseCaseService;

    String testName = "John";
    String testSurname = "Doe";
    String testTitle1 = "Tytuł";
    String testTitle2 = "Butelka";
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

    UUID testOfferId1 = null;
    UUID testOfferId2 = null;
    UUID testPhotoId = null;
    UUID testAddressId = null;
    UUID testOwnerId1 = null;
    UUID testOwnerPhotoId = null;
    UUID testReceiverId1 = null;
    UUID testReceiverId2 = null;
    UUID testReviewId1 = null;
    UUID testReviewId2 = null;

    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_invalid_offers_matching_criteria_and_0() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            testOfferId1 = UUID.randomUUID();
            testOfferId2 = UUID.randomUUID();
            testPhotoId = UUID.randomUUID();
            testAddressId = UUID.randomUUID();
            testOwnerId1 = UUID.randomUUID();
            testOwnerPhotoId = UUID.randomUUID();
            testReceiverId1 = UUID.randomUUID();
            testReceiverId2 = UUID.randomUUID();
            testReviewId1 = UUID.randomUUID();
            testReviewId2 = UUID.randomUUID();

            test_offerGetDto1 = new OfferGetDto(
                    testOfferId1,
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
                    testTitle1,
                    testCondition,
                    testCategory,
                    testDescription,
                    testPhotoId,
                    testOwnerId1,
                    testName,
                    testSurname,
                    testOwnerPhotoId,
                    testReceiverId1,
                    testDate,
                    testReviewId1,
                    testReviewValue,
                    testDate
            );

            test_offerGetDto2 = new OfferGetDto(
                    testOfferId2,
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
                    testTitle2,
                    testCondition,
                    testCategory,
                    testDescription,
                    testPhotoId,
                    testOwnerId1,
                    testName,
                    testSurname,
                    testOwnerPhotoId,
                    testReceiverId2,
                    testDate,
                    testReviewId2,
                    testReviewValue,
                    testDate
            );

            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto1)).thenThrow(MultipleValidationException.class);
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2)).thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            Assertions.assertEquals(0, test_getOwnerReviewCountUseCaseService.getUserReviewCount(testOwnerId1));
        }
    }

    @Test
    void get_two_offers_but_one_with_wrong_status_and_return_1() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId1 = UUID.randomUUID();
        testReceiverId2 = UUID.randomUUID();
        testReviewId1 = UUID.randomUUID();
        testReviewId2 = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId1,
                testDate,
                testReviewId1,
                testReviewValue,
                testDate
        );

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle2,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId2,
                testDate,
                testReviewId2,
                testReviewValue,
                testDate
        );

        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(1, test_getOwnerReviewCountUseCaseService.getUserReviewCount(testOwnerId1));
    }

    @Test
    void get_two_offers_but_one_with_different_owner_and_return_1()
    {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId1 = UUID.randomUUID();
        testReceiverId2 = UUID.randomUUID();
        testReviewId1 = UUID.randomUUID();
        testReviewId2 = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId1,
                testDate,
                testReviewId1,
                testReviewValue,
                testDate
        );

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle2,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                UUID.randomUUID(),
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId2,
                testDate,
                testReviewId2,
                testReviewValue,
                testDate
        );

        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(1, test_getOwnerReviewCountUseCaseService.getUserReviewCount(testOwnerId1));
    }

    @Test
    void get_two_offers_but_one_with_null_review_and_return_1()
    {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId1 = UUID.randomUUID();
        testReceiverId2 = UUID.randomUUID();
        testReviewId1 = UUID.randomUUID();
        testReviewId2 = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId1,
                testDate,
                testReviewId1,
                testReviewValue,
                testDate
        );

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle2,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId2,
                testDate,
                null,
                testReviewValue,
                testDate
        );

        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(1, test_getOwnerReviewCountUseCaseService.getUserReviewCount(testOwnerId1));
    }

    @Test
    void get_two_valid_offers_and_return_2()
    {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId1 = UUID.randomUUID();
        testReceiverId2 = UUID.randomUUID();
        testReviewId1 = UUID.randomUUID();
        testReviewId2 = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId1,
                testDate,
                testReviewId1,
                testReviewValue,
                testDate
        );

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle2,
                testCondition,
                testCategory,
                testDescription,
                testPhotoId,
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                testReceiverId2,
                testDate,
                testReviewId2,
                testReviewValue,
                testDate
        );

        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(2, test_getOwnerReviewCountUseCaseService.getUserReviewCount(testOwnerId1));
    }
}

