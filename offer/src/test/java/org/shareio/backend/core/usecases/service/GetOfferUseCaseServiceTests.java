package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.Mockito.when;

public class GetOfferUseCaseServiceTests {
    private AutoCloseable closeable;
    OfferGetDto expectedCorrectOfferGetDto = null;
    OfferResponseDto expectedCorrectOfferResponseDto = null;
    private GetOfferUseCaseService getOfferUseCaseService = null;

    private UUID testOfferId = null;
    private UUID testPhotoId = null;
    private UUID testOwnerId = null;
    private UUID testOwnerPhotoId = null;

    LocalDateTime testTime = LocalDateTime.now();
    String testStatus = Status.CREATED.toString();
    String testCity = "Zgierz";
    String testStreet = "Zgierska";
    String testHouseNumber = "12";
    Double testLongitude = 0.0;
    Double testLatitude = 0.0;
    String testTitle = "Szop";
    String testConditionRaw = Condition.ALMOST_NEW.toString();
    String testCategoryRaw = Category.HOBBY.toString();
    String testConditionPolish = Condition.ALMOST_NEW.polishName();
    String testCategoryPolish = Category.HOBBY.polishName();
    String testDescription = "Testing description with minimum of 20 characters";
    String testOwnerName = "John";
    String testOwnerSurname = "Doe";
    Double testOwnerRating = 2.0;
    Integer testOwnerReviewCount = 6;
    String testDistance = "1.5";
    ArrayList<OfferGetDto> offersList = new ArrayList<>();

    @Mock
    GetOfferDaoInterface getOfferDaoInterface;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        getOfferUseCaseService = new GetOfferUseCaseService(getOfferDaoInterface);

        testOfferId = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();
        UUID incorrectOfferId = UUID.randomUUID();

        expectedCorrectOfferGetDto = new OfferGetDto(testOfferId, testTime, testStatus, testCity, testStreet, testHouseNumber
                , testLongitude, testLatitude, testTitle, testConditionRaw, testCategoryRaw, testDescription, testPhotoId, testOwnerId, testOwnerName
                , testOwnerSurname, testOwnerPhotoId, testOwnerRating, testOwnerReviewCount, testTime);
        expectedCorrectOfferResponseDto = new OfferResponseDto(testOfferId, testTime, testStatus, testCity, testStreet, testDistance
                , testLongitude, testLatitude, testTitle, testConditionPolish, testCategoryPolish, testDescription, testPhotoId,
                testOwnerId, testOwnerName, testOwnerSurname, testOwnerPhotoId, testOwnerRating, testOwnerReviewCount, testTime);

        when(getOfferDaoInterface.getOfferDto(testOfferId)).thenReturn(Optional.of(expectedCorrectOfferGetDto));
        when(getOfferDaoInterface.getOfferDto(incorrectOfferId)).thenThrow(new NoSuchElementException());
    }

    @AfterEach
    public void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void getOfferResponseDtoCorrectTest() {
        Assertions.assertDoesNotThrow(() ->
        {
            OfferResponseDto actualOfferResponseDto = getOfferUseCaseService.getOfferResponseDto(testOfferId);
            Assertions.assertEquals(expectedCorrectOfferResponseDto.offerId(), actualOfferResponseDto.offerId());
            Assertions.assertEquals(expectedCorrectOfferResponseDto.city(), actualOfferResponseDto.city());
            Assertions.assertEquals(expectedCorrectOfferResponseDto.category(), actualOfferResponseDto.category());
            Assertions.assertEquals(expectedCorrectOfferResponseDto.description(), actualOfferResponseDto.description());
        });
    }

    @Test
    public void getOfferResponseDtoThrowsNoSuchElementExceptionTest() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
        {
            getOfferUseCaseService.getOfferResponseDto(UUID.randomUUID());
        });
    }
}
