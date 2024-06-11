package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetOffersByUserDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GetOffersByUserAndStatusUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetOffersByUserDaoInterface test_getOffersByUserDaoInterface;
    @Mock
    OfferGetDto test_offerGetDto1;
    @Mock
    OfferGetDto test_offerGetDto2;
    @InjectMocks
    GetOffersByUserAndStatusUseCaseService test_getOffersByUserAndStatusUseCaseService;

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
    UUID testOwnerId2 = null;
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
    void get_one_invalid_offer_matching_criteria_and_throw_no_such_element_exception() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            testOfferId1 = UUID.randomUUID();
            testOfferId2 = UUID.randomUUID();
            testPhotoId = UUID.randomUUID();
            testAddressId = UUID.randomUUID();
            testOwnerId1 = UUID.randomUUID();
            testOwnerId2 = UUID.randomUUID();
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
                    testOwnerId1,
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
                    testOwnerId2,
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
            when(test_getOffersByUserDaoInterface.getOffersByUser(testOwnerId1)).thenReturn(List.of(test_offerGetDto1));
            Assertions.assertThrows(NoSuchElementException.class, () ->
                    test_getOffersByUserAndStatusUseCaseService.getOfferResponseDtoListByUser(testOwnerId1, Status.CREATED)
            );
        }
    }

    @Test
    void get_no_offers_with_matching_id_and_throw_no_such_element_exception() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerId2 = UUID.randomUUID();
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
                testOwnerId1,
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
                testOwnerId2,
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
        when(test_getOffersByUserDaoInterface.getOffersByUser(randomUUID)).thenReturn(List.of());
        Assertions.assertThrows(NoSuchElementException.class, () -> test_getOffersByUserAndStatusUseCaseService.getOfferResponseDtoListByUser(randomUUID, Status.CREATED));
    }

    @Test
    void get_offer_with_matching_id_but_not_matching_status_and_return_empty_list() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerId2 = UUID.randomUUID();
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
                testOwnerId1,
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
                testOwnerId2,
                testName,
                testSurname,
                testOwnerPhotoId,
                null,
                null,
                null,
                testReviewValue,
                testDate
        );

        when(test_getOffersByUserDaoInterface.getOffersByUser(testOwnerId1)).thenReturn(List.of(test_offerGetDto1));
        Assertions.assertEquals(0, test_getOffersByUserAndStatusUseCaseService.getOfferResponseDtoListByUser(testOwnerId1, Status.RESERVED).size());
    }

    @Test
    void get_offer_with_matching_id_and_status_and_return_its_id() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerId2 = UUID.randomUUID();
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
                testOwnerId1,
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
                testOwnerId2,
                testName,
                testSurname,
                testOwnerPhotoId,
                null,
                null,
                null,
                testReviewValue,
                testDate
        );

        when(test_getOffersByUserDaoInterface.getOffersByUser(testOwnerId1)).thenReturn(List.of(test_offerGetDto1));
        Assertions.assertNotNull(test_getOffersByUserAndStatusUseCaseService.getOfferResponseDtoListByUser(testOwnerId1, Status.CREATED).getFirst());
    }

    @Test
    void get_two_offers_with_matching_ids_and_status_and_return_them_both() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
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
                testOwnerId1,
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
                testOwnerId1,
                testName,
                testSurname,
                testOwnerPhotoId,
                null,
                null,
                null,
                testReviewValue,
                testDate
        );

        when(test_getOffersByUserDaoInterface.getOffersByUser(testOwnerId1)).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(2, test_getOffersByUserAndStatusUseCaseService.getOfferResponseDtoListByUser(testOwnerId1, Status.CREATED).size());
    }
}
