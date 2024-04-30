package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


public class OfferAdapterTests {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private UserEntity userEntity;
    @Mock
    private AddressEntity addressEntity;
    private AutoCloseable closeable;
    private OfferAdapter offerAdapter;
    private UUID offerId = null;
    private OfferGetDto offerGetDto;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        offerId = UUID.randomUUID();
        OfferEntity offerEntity = new OfferEntity(
                null,
                offerId,
                userEntity,
                addressEntity,
                LocalDateTime.now(),
                Status.CREATED,
                null,
                null,
                "testOffer",
                Condition.ALMOST_NEW,
                "testing purposes",
                UUID.randomUUID()
        );
        offerGetDto = Optional.of(offerEntity).map(OfferDatabaseMapper::toDto).get();
        offerAdapter = new OfferAdapter(offerRepository);
        when(offerRepository.findByOfferId(offerId)).thenReturn(Optional.of(offerEntity));
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetOfferDtoThrows() {
        UUID userIdWrong = UUID.randomUUID();
        Assert.assertThrows(NoSuchElementException.class, () -> offerAdapter.getOfferDto(userIdWrong));
    }


//    @Test
//    public void testGetUserDtoCorrect() {
//        Assertions.assertTrue(offerAdapter.getOfferDto(offerId).isPresent());
//        OfferGetDto actual = offerAdapter.getOfferDto(offerId).get();
//        Assertions.assertEquals(offerGetDto.offerId(), actual.offerId());
//        //TODO: FINISH THIS
//}
}
