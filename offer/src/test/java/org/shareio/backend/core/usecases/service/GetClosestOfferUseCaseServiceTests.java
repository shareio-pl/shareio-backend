package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.Mockito.when;

public class GetClosestOfferUseCaseServiceTests {

    private AutoCloseable closeable;
    OfferGetDto expectedCorrectOfferGetDto1 = null;
    OfferGetDto expectedCorrectOfferGetDto2 = null;
    OfferResponseDto expectedCorrectOfferResponseDto = null;
    private GetClosestOfferUseCaseService getClosestOfferUseCaseService = null;

    private UUID testOffer1Id = null;
    private UUID testOffer2Id = null;
    private UUID testPhotoId = null;
    private UUID testOwnerId = null;
    private UUID testOwnerPhotoId = null;

    LocalDateTime testTime = LocalDateTime.now();
    String testStatus = Status.CREATED.toString();
    String testCity = "Zgierz";
    String testStreet = "Zgierska";
    String testHouseNumber = "12";
    Double testLongitude1 = 0.0;
    Double testLatitude1 = 0.0;
    Double testLongitude2 = 1.0;
    Double testLatitude2 = 0.0;
    String testTitle1 = "Szop";
    String testTitle2 = "!Szop";
    String testConditionRaw = Condition.ALMOST_NEW.toString();
    String testCategoryRaw = Category.HOBBY.toString();
    String testConditionPolish = Condition.ALMOST_NEW.polishName();
    String testCategoryPolish = Category.HOBBY.polishName();
    String testDescription = "desc";
    String testOwnerName = "John";
    String testOwnerSurname = "Doe";
    Double testOwnerRating = 2.0;
    Integer testOwnerReviewCount = 6;
    String testDistance = "1.5";
    ArrayList<OfferGetDto> offersList = new ArrayList<>();


    @Mock
    GetAllOffersDaoInterface getAllOffersDaoInterface;
    @Mock
    GetLocationDaoInterface getLocationDaoInterface;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        getClosestOfferUseCaseService = new GetClosestOfferUseCaseService(getAllOffersDaoInterface, getLocationDaoInterface);

        testOffer1Id = UUID.randomUUID();
        testOffer2Id = UUID.randomUUID();
        testPhotoId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testOwnerPhotoId = UUID.randomUUID();

        expectedCorrectOfferGetDto1 = new OfferGetDto(testOffer1Id, testTime, testStatus, testCity, testStreet, testHouseNumber
                , testLongitude1, testLatitude1, testTitle1, testConditionRaw, testCategoryRaw, testDescription, testPhotoId, testOwnerId, testOwnerName
                , testOwnerSurname, testOwnerPhotoId, testOwnerRating, testOwnerReviewCount, testTime);
        offersList.add(expectedCorrectOfferGetDto1);
        expectedCorrectOfferGetDto2 = new OfferGetDto(testOffer2Id, testTime, testStatus, testCity, testStreet, testHouseNumber
                , testLongitude2, testLatitude2, testTitle2, testConditionRaw, testCategoryRaw, testDescription, testPhotoId, testOwnerId, testOwnerName
                , testOwnerSurname, testOwnerPhotoId, testOwnerRating, testOwnerReviewCount, testTime);
        offersList.add(expectedCorrectOfferGetDto2);

        expectedCorrectOfferResponseDto = new OfferResponseDto(testOffer1Id, testTime, testStatus, testCity, testStreet, testDistance
                , testLongitude1, testLatitude1, testTitle1, testConditionPolish, testCategoryPolish, testDescription, testPhotoId, testOwnerId, testOwnerName
                , testOwnerSurname, testOwnerPhotoId, testOwnerRating, testOwnerReviewCount, testTime);

        when(getAllOffersDaoInterface.getAllOffers()).thenReturn(offersList);
    }

    @AfterEach
    public void closeService() throws Exception {
        closeable.close();
    }
}
