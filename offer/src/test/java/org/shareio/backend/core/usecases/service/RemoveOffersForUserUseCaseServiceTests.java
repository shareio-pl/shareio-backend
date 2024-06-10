package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveOfferCommandInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class RemoveOffersForUserUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetAllOffersDaoInterface test_getAllOffersDaoInterface;
    @Mock
    RemoveOfferCommandInterface test_removeOfferCommandInterface;
    @Mock
    OfferGetDto test_offerGetDto1;
    @Mock
    OfferGetDto test_offerGetDto2;
    @Mock
    RemoveResponseDto test_removeResponseDto;
    @InjectMocks
    RemoveOffersForUserUseCaseService test_removeOffersForUserUseCaseService;

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
    void get_no_offers_for_user_and_dont_delete_anything() {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerId2 = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReviewId = UUID.randomUUID();

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
                null,
                null,
                testReviewId,
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
        test_removeResponseDto = new RemoveResponseDto();

        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(test_removeResponseDto, test_removeOffersForUserUseCaseService.removeOffersForUser(UUID.randomUUID(), test_removeResponseDto));
        verify(test_removeOfferCommandInterface, times(0)).removeOffer(any());
        Assertions.assertEquals(0, test_removeResponseDto.getDeletedReviewCount());
        Assertions.assertEquals(0, test_removeResponseDto.getDeletedAddressCount());
        Assertions.assertEquals(0, test_removeResponseDto.getDeletedOfferCount());
    }

    @Test
    void get_one_offer_for_user_with_no_review_and_delete_it()
    {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerId2 = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReviewId = UUID.randomUUID();

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
                null,
                null,
                testReviewId,
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
        test_removeResponseDto = new RemoveResponseDto();

        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(test_removeResponseDto, test_removeOffersForUserUseCaseService.removeOffersForUser(testOwnerId2, test_removeResponseDto));
        verify(test_removeOfferCommandInterface, times(1)).removeOffer(any());
        Assertions.assertEquals(0, test_removeResponseDto.getDeletedReviewCount());
        Assertions.assertEquals(1, test_removeResponseDto.getDeletedAddressCount());
        Assertions.assertEquals(1, test_removeResponseDto.getDeletedOfferCount());
    }

    @Test
    void get_one_offer_for_user_with_review_and_delete_it()
    {
        testOfferId1 = UUID.randomUUID();
        testOfferId2 = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId1 = UUID.randomUUID();
        testOwnerId2 = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReviewId = UUID.randomUUID();

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
                null,
                null,
                testReviewId,
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
        test_removeResponseDto = new RemoveResponseDto();

        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        Assertions.assertEquals(test_removeResponseDto, test_removeOffersForUserUseCaseService.removeOffersForUser(testOwnerId1, test_removeResponseDto));
        verify(test_removeOfferCommandInterface, times(1)).removeOffer(any());
        Assertions.assertEquals(1, test_removeResponseDto.getDeletedReviewCount());
        Assertions.assertEquals(1, test_removeResponseDto.getDeletedAddressCount());
        Assertions.assertEquals(1, test_removeResponseDto.getDeletedOfferCount());
    }
}
