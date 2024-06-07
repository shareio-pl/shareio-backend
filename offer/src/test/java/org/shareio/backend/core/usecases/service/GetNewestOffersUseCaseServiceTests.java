package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

class GetNewestOffersUseCaseServiceTests {

    AutoCloseable test_autoCloseable;

    @Mock
    GetAllOffersDaoInterface test_getAllOffersDaoInterface;
    @Mock
    OfferGetDto test_offerGetDto1;
    @Mock
    OfferGetDto test_offerGetDto2;
    @Mock
    OfferGetDto test_offerGetDto3;
    @InjectMocks
    GetNewestOffersUseCaseService test_getNewestOffersUseCaseService;

    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_no_valid_offers_and_throw_IllegalArgumentException() {
        try (MockedStatic<OfferValidator> utilities = Mockito.mockStatic(OfferValidator.class)) {
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto1))
                    .thenThrow(MultipleValidationException.class);
            utilities.when(() -> OfferValidator.validateOffer(test_offerGetDto2))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> test_getNewestOffersUseCaseService.getNewestOffers());

        }
    }

    @Test
    void get_two_valid_offers_and_return_two_element_list() {
        test_offerGetDto1 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                5.0,
                3.0,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        test_offerGetDto2 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                0.1,
                0.1,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2));
        List<UUID> offerList = Assertions.assertDoesNotThrow(
                () -> test_getNewestOffersUseCaseService.getNewestOffers());
        Assertions.assertEquals(2, offerList.size());

    }

    @Test
    void get_three_valid_offers_one_not_CREATED_and_return_two_element_list() {
        test_offerGetDto1 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                5.0,
                3.0,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        test_offerGetDto2 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.FINISHED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                0.1,
                0.1,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        test_offerGetDto3 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                5.0,
                3.0,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2, test_offerGetDto3));
        List<UUID> offerList = Assertions.assertDoesNotThrow(
                () -> test_getNewestOffersUseCaseService.getNewestOffers());
        Assertions.assertEquals(2, offerList.size());
        Assertions.assertFalse(offerList.contains(null));
    }

    @Test
    void get_three_valid_offers_two_older_than_allowed_and_return_two_element_list() {
        test_offerGetDto1 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now(),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                5.0,
                3.0,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        test_offerGetDto2 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now().minus(Const.OFFER_RESERVATION_DURATION.plus(Const.OFFER_RESERVATION_DURATION)),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                0.1,
                0.1,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        test_offerGetDto3 = new OfferGetDto(
                UUID.randomUUID(),
                LocalDateTime.now().minus(Const.OFFER_RESERVATION_DURATION.plus(Const.OFFER_RESERVATION_DURATION)),
                Status.CREATED.toString(),
                UUID.randomUUID(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Kołodziejska",
                "18",
                "3",
                "91-001",
                5.0,
                3.0,
                "Mieszkanie",
                "BROKEN",
                "OTHER",
                "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Jan",
                "Kowalski",
                UUID.randomUUID(),
                null,
                null,
                UUID.randomUUID(),
                4.5,
                LocalDateTime.now()
        );
        when(test_getAllOffersDaoInterface.getAllOffers()).thenReturn(List.of(test_offerGetDto1, test_offerGetDto2, test_offerGetDto3));
        List<UUID> offerList = Assertions.assertDoesNotThrow(
                () -> test_getNewestOffersUseCaseService.getNewestOffers());
        Assertions.assertEquals(2, offerList.size());
        Assertions.assertFalse(offerList.contains(null));
    }


}
