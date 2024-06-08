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
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferModifyDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GetOfferOwnerIdUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetOfferDaoInterface test_getOfferDaoInterface;
    @Mock
    OfferGetDto test_offerGetDto;
    @InjectMocks
    GetOfferOwnerIdUseCaseService test_getOfferOwnerIdUseCaseService;

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
            Assertions.assertThrows(MultipleValidationException.class, () -> test_getOfferOwnerIdUseCaseService.getOfferOwnerId(testOfferId));
        }
    }

    @Test
    void get_no_offer_for_id_and_throw_no_such_element_exception() {
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
        when(test_getOfferDaoInterface.getOfferDto(randomUUID)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> test_getOfferDaoInterface.getOfferDto(randomUUID));
    }

    @Test
    void get_valid_offer_for_id_and_return_its_owner_id() {
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
        try {
            Assertions.assertEquals(testOwnerId, test_getOfferOwnerIdUseCaseService.getOfferOwnerId(testOfferId));
        } catch (MultipleValidationException e) {
            Assertions.fail();
        }
    }
}
