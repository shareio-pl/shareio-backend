package org.shareio.backend.core.usecases.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferReserveDto;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveOfferCommandInterface;
import org.shareio.backend.core.usecases.port.out.RemoveReviewCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class RemoveOfferUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetOfferDaoInterface test_getOfferDaoInterface;
    @Mock
    RemoveOfferCommandInterface test_removeOfferCommandInterface;
    @Mock
    RemoveReviewCommandInterface test_removeReviewCommandInterface;
    @Mock
    OfferGetDto test_offerGetDto;
    @Mock
    RemoveResponseDto test_removeResponseDto;
    @InjectMocks
    RemoveOfferUseCaseService test_removeOfferUseCaseService;

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
    void get_no_offer_for_id_and_thrown_no_such_element_exception() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId = UUID.randomUUID();
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
                null,
                null,
                testReviewId,
                testReviewValue,
                testDate
        );
        test_removeResponseDto = new RemoveResponseDto();

        UUID randomUUID = UUID.randomUUID();
        when(test_getOfferDaoInterface.getOfferDto(randomUUID)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> test_removeOfferUseCaseService.removeOffer(randomUUID, test_removeResponseDto));
    }

    @Test
    void get_offer_for_id_with_review_and_remove_it() {
        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        testReceiverId = UUID.randomUUID();
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
                null,
                null,
                testReviewId,
                testReviewValue,
                testDate
        );
        test_removeResponseDto = new RemoveResponseDto();

        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));

        Assertions.assertEquals(test_removeResponseDto, test_removeOfferUseCaseService.removeOffer(testOfferId, test_removeResponseDto));
        verify(test_removeReviewCommandInterface, times(1)).removeReview(any());
        verify(test_removeOfferCommandInterface, times(1)).removeOffer(any());
        Assertions.assertEquals(1, test_removeResponseDto.getDeletedReviewCount());
        Assertions.assertEquals(1, test_removeResponseDto.getDeletedOfferCount());
    }
}
