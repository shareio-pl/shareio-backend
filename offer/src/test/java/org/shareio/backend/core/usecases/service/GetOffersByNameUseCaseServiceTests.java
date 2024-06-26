package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetOffersByNameDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GetOffersByNameUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetOffersByNameDaoInterface test_getOffersByNameDaoInterface;
    @Mock
    OfferGetDto test_offerGetDto1;
    @Mock
    OfferGetDto test_offerGetDto2;
    @InjectMocks
    GetOffersByNameUseCaseService test_getOffersByNameUseCaseService;

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
    void get_invalid_offer_with_matching_name_and_return_empty_list() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            testOfferId1 = UUID.randomUUID();
            testPhotoId = UUID.randomUUID();
            testAddressId = UUID.randomUUID();
            testOwnerId = UUID.randomUUID();
            testOwnerPhotoId = UUID.randomUUID();

            test_offerGetDto1 = new OfferGetDto(
                    testOfferId1,
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
                    testTitle1,
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

            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto1)).thenThrow(MultipleValidationException.class);
            when(test_getOffersByNameDaoInterface.getOffersByName(testTitle1)).thenReturn(List.of(test_offerGetDto1));
            Assertions.assertEquals(0, test_getOffersByNameUseCaseService.getOfferResponseDtoListByName(testTitle1).size());
        }
    }

    @Test
    void get_no_offers_with_matching_name_and_return_empty_list() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
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

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle2,
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

        when(test_getOffersByNameDaoInterface.getOffersByName("test")).thenReturn(List.of());
        Assertions.assertEquals(0, test_getOffersByNameUseCaseService.getOfferResponseDtoListByName(testTitle1).size());
    }

    @Test
    void get_one_offer_with_matching_name_but_not_CREATED_and_return_empty_list() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
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

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle2,
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

        when(test_getOffersByNameDaoInterface.getOffersByName(testTitle1)).thenReturn(List.of(test_offerGetDto1));
        Assertions.assertEquals(0, test_getOffersByNameUseCaseService.getOfferResponseDtoListByName(testTitle1).size());
    }

    @Test
    void get_one_offer_with_matching_name_and_return_its_id() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
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

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle2,
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

        when(test_getOffersByNameDaoInterface.getOffersByName(testTitle1)).thenReturn(List.of(test_offerGetDto1));
        UUID matchingNameId = Assertions.assertDoesNotThrow(() -> test_getOffersByNameUseCaseService.getOfferResponseDtoListByName(testTitle1).getFirst());
        Assertions.assertNotNull(matchingNameId);
    }

    @Test
    void get_two_offers_with_matching_name_and_return_two_ids()
    {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

        test_offerGetDto1 = new OfferGetDto(
                testOfferId1,
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
                testTitle1,
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

        test_offerGetDto2 = new OfferGetDto(
                testOfferId2,
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
                testTitle1,
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

        when(test_getOffersByNameDaoInterface.getOffersByName(testTitle1)).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(2, test_getOffersByNameUseCaseService.getOfferResponseDtoListByName(testTitle1).size());
    }
}
