package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.AddressSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferModifyDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferChangeMetadataCommandInterface;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class ModifyOfferUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetOfferDaoInterface test_getOfferDaoInterface;
    @Mock
    UpdateOfferChangeMetadataCommandInterface test_updateOfferChangeMetadataCommandInterface;
    @Mock
    OfferGetDto test_offerGetDto;
    @Mock
    AddressSaveDto test_addressSaveDto;
    @Mock
    AddressSaveDto test_changedAddressSaveDto;
    @Mock
    OfferModifyDto test_offerModifyDto;
    @InjectMocks
    ModifyOfferUseCaseService test_modifyOfferUseCaseService;
    @Captor
    ArgumentCaptor<OfferSnapshot> test_offerSnapshotCaptor;

    String testName = "John";
    String testSurname = "Doe";
    String testTitle = "Tytuł";
    String testChangedTitle = "Zmieniony tytuł";
    String testCondition = "BROKEN";
    String testChangedCondition = "NEW";
    String testCategory = "OTHER";
    String testChangedCategory = "HOBBY";
    String testDescription = "Testowy opis, który zawiera przynajmniej 20 znaków";
    String testChangedDescription = "Zmieniony opis, który zawiera przynajmniej 20 znaków";
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
    Condition testChangedConditionActual = Condition.NEW;
    Category testChangedCategoryActual = Category.HOBBY;

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

            test_offerModifyDto = new OfferModifyDto(
                    test_addressSaveDto,
                    testChangedTitle,
                    testCondition,
                    testCategory,
                    testDescription
            );

            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto)).thenThrow(MultipleValidationException.class);
            when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
            Assertions.assertThrows(MultipleValidationException.class, () -> test_modifyOfferUseCaseService.modifyOffer(testOfferId, test_offerModifyDto));
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

        test_offerModifyDto = new OfferModifyDto(
                test_addressSaveDto,
                testChangedTitle,
                testCondition,
                testCategory,
                testDescription
        );

        UUID randomUUID = UUID.randomUUID();
        when(test_getOfferDaoInterface.getOfferDto(randomUUID)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> test_modifyOfferUseCaseService.modifyOffer(UUID.randomUUID(), test_offerModifyDto));
    }

    @Test
    void get_offer_but_with_status_CANCELED_and_throw_no_such_element_exception() {
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

        test_offerModifyDto = new OfferModifyDto(
                test_addressSaveDto,
                testChangedTitle,
                testCondition,
                testCategory,
                testDescription
        );

        when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
        Assertions.assertThrows(NoSuchElementException.class, () -> test_modifyOfferUseCaseService.modifyOffer(testOfferId, test_offerModifyDto));
    }

    @Test
    void get_and_modify_correct_offer() {
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

        test_offerModifyDto = new OfferModifyDto(
                test_changedAddressSaveDto,
                testChangedTitle,
                testChangedCondition,
                testChangedCategory,
                testChangedDescription
        );


        try (MockedStatic<LocationCalculator> test_locationCalculator = Mockito.mockStatic(LocationCalculator.class)) {
            when(test_getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(test_offerGetDto));
            test_locationCalculator.when(() -> LocationCalculator.getLocationFromAddress(
                            test_changedAddressSaveDto.country(),
                            test_changedAddressSaveDto.city(),
                            test_changedAddressSaveDto.street(),
                            test_changedAddressSaveDto.houseNumber()))
                    .thenReturn(new Location(testLatitude, testLongitude));

            Assertions.assertDoesNotThrow(() -> test_modifyOfferUseCaseService.modifyOffer(testOfferId, test_offerModifyDto));
            verify(test_updateOfferChangeMetadataCommandInterface).updateOfferMetadata(test_offerSnapshotCaptor.capture());
            OfferSnapshot modifiedOffer = test_offerSnapshotCaptor.getValue();

            Assertions.assertEquals(testChangedTitle, modifiedOffer.title());
            Assertions.assertEquals(testChangedConditionActual, modifiedOffer.condition());
            Assertions.assertEquals(testChangedCategoryActual, modifiedOffer.category());
            Assertions.assertEquals(testChangedDescription, modifiedOffer.description());
            Assertions.assertEquals(test_changedAddressSaveDto.city(), modifiedOffer.address().getCity());
            Assertions.assertEquals(testLatitude, modifiedOffer.address().getLocation().getLatitude());
        }


    }
}
