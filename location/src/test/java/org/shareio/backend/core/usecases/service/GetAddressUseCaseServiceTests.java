package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;
import org.shareio.backend.core.usecases.port.out.GetAddressDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetAddressUseCaseServiceTests {

    private AutoCloseable closeable;
    AddressGetDto expectedCorrectAddressGetDto = null;
    AddressResponseDto expectedCorrectAddressResponseDto = null;
    private GetAddressUseCaseService getAddressUseCaseService;


    @Mock
    GetAddressDaoInterface getAddressDaoInterface;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        UUID incorrectUserId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        closeable = openMocks(this);
        getAddressUseCaseService = new GetAddressUseCaseService(getAddressDaoInterface);
        expectedCorrectAddressGetDto = new AddressGetDto(
                addressId,
                "Polska",
                "Łódzkie",
                "Łódź",
                "Wólczańska",
                "215",
                "1",
                "91-001");
        expectedCorrectAddressResponseDto = new AddressResponseDto(
                "Polska",
                "Łódzkie",
                "Łódź",
                "Wólczańska",
                "215",
                "1",
                "91-001"
        );


        when(getAddressDaoInterface.getAddressDto(addressId)).thenReturn(Optional.of(expectedCorrectAddressGetDto));
        when(getAddressDaoInterface.getAddressDto(incorrectUserId)).thenThrow(new NoSuchElementException());
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void get_nonexistent_address_and_throw_NoSuchElementException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> getAddressUseCaseService.getAddressResponseDto(UUID.randomUUID()));
    }

    @Test
    public void get_invalid_address_and_throw_MultipleValidationErrorException() {
        UUID incorrectStringId = UUID.randomUUID();
        AddressGetDto expectedIncorrectNameUserProfileGetDto = new AddressGetDto(
                incorrectStringId,
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " "
        );
        when(getAddressDaoInterface.getAddressDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getAddressUseCaseService.getAddressResponseDto(incorrectStringId));
        Assertions.assertEquals(6, multipleValidationException.getErrorMap().size());
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Country"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Region"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("City"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Street"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("HouseNumber"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("FlatNumber"));
    }

}

