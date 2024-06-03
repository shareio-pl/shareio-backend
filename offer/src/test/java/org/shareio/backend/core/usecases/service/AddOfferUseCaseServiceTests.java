package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveResponseDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.SaveOfferCommandInterface;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddOfferUseCaseServiceTests {
    AutoCloseable test_autoCloseable;

    @Mock
    GetUserProfileDaoInterface test_getUserProfileDaoInterface;
    @Mock
    SaveOfferCommandInterface test_saveOfferCommandInterface;
    @InjectMocks
    AddOfferUseCaseService test_addOfferUseCaseService;
    @Mock
    OfferSaveDto test_offerSaveDto;
    @Captor
    ArgumentCaptor<OfferSnapshot> test_offerSnapshotCaptor;

    String testEmail = "test@gmail.com";
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
    Location testLocation = new Location(1.5, 1.0);

    UUID testUserId = null;
    UUID testOfferId = null;
    UUID testPhotoId = null;
    UUID testAddressId = null;

    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_invalid_dto_and_throw_NoSuchElementException() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerSaveDto))
                    .thenThrow(MultipleValidationException.class);
            Assertions.assertThrows(MultipleValidationException.class,
                    () -> test_addOfferUseCaseService.addOffer(test_offerSaveDto, UUID.randomUUID())
            );
        }
    }

    @Test
    void get_valid_dto_and_verify_setters_and_succeed() {
        testUserId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOfferId = UUID.randomUUID();
        testAddressId = UUID.randomUUID();
        when(test_getUserProfileDaoInterface.getUserDto(testUserId)).thenReturn(Optional.of(new UserProfileGetDto(
                testUserId,
                testEmail,
                testName,
                testSurname,
                null,
                testPhotoId,
                testAddressId,
                null,
                null
        )));
        test_offerSaveDto = new OfferSaveDto(
                testUserId,
                testTitle,
                testCondition,
                testCategory,
                testDescription,
                testCountry,
                testRegion,
                testCity,
                testStreet,
                testHouseNumber
        );
        try (MockedStatic<LocationCalculator> locationCalculator = Mockito.mockStatic(LocationCalculator.class)) {
            locationCalculator.when(() -> LocationCalculator.getLocationFromAddress(any(), any(), any(), any())).thenReturn(testLocation);

            OfferSaveResponseDto offerSaveResponseDto = Assertions.assertDoesNotThrow(
                    () -> test_addOfferUseCaseService.addOffer(test_offerSaveDto, testPhotoId)
            );
            verify(test_saveOfferCommandInterface, times(1)).saveOffer(any());
            verify(test_saveOfferCommandInterface).saveOffer(test_offerSnapshotCaptor.capture());

            OfferSnapshot offerSnapshotCaptorValue = test_offerSnapshotCaptor.getValue();
            Assertions.assertNotNull(offerSnapshotCaptorValue.owner());
            Assertions.assertEquals(testUserId, offerSnapshotCaptorValue.owner().userId().getId());
            Assertions.assertNotNull(offerSnapshotCaptorValue.photoId());
            Assertions.assertEquals(testPhotoId, offerSnapshotCaptorValue.photoId().getId());
            Assertions.assertEquals(testLocation, offerSnapshotCaptorValue.address().getLocation());

            locationCalculator.verify(
                    () -> LocationCalculator.getLocationFromAddress(any(), any(), any(), any()),
                    times(1)
            );
            Assertions.assertNotNull(offerSaveResponseDto);
        }


    }

}
