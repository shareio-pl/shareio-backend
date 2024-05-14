package org.shareio.backend.infrastructure.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;
import org.shareio.backend.core.usecases.port.out.GetAddressDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.core.usecases.service.GetAddressUseCaseService;
import org.shareio.backend.core.usecases.service.GetLocationUseCaseService;
import org.shareio.backend.infrastructure.dbadapter.repositories.AddressRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

class AddressRESTControllerTests {

    AutoCloseable autoCloseable;

    @Mock
    AddressRepository addressRepository;

    @Mock
    private GetAddressDaoInterface getAddressDaoInterface;

    @Mock
    private GetLocationDaoInterface getLocationDaoInterface;

    @MockBean
    @InjectMocks
    private GetAddressUseCaseService getAddressUseCaseInterface;

    @MockBean
    @InjectMocks
    private GetLocationUseCaseService getLocationUseCaseInterface;

    @InjectMocks
    AddressRESTController addressRESTController;

    UUID addressInvalidId;
    UUID addressValidId;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        addressInvalidId = UUID.randomUUID();
        addressValidId = UUID.randomUUID();
        autoCloseable = MockitoAnnotations.openMocks(this);
        addressRESTController = new AddressRESTController(getAddressUseCaseInterface, getLocationUseCaseInterface);
        when(getAddressDaoInterface.getAddressDto(addressInvalidId)).thenReturn(
                Optional.of(new AddressGetDto(
                        addressInvalidId,
                        " ",
                        "Łódzkie",
                        "Łódź",
                        "Wólczańska",
                        "215",
                        "1",
                        "91-001"
                ))
        );

        when(getAddressDaoInterface.getAddressDto(addressValidId)).thenReturn(
                Optional.of(new AddressGetDto(
                        addressValidId,
                        "Polska",
                        "Łódzkie",
                        "Łódź",
                        "Wólczańska",
                        "215",
                        "1",
                        "91-001"
                ))
        );

        when(getLocationDaoInterface.getLocationDto(addressInvalidId)).thenReturn(
                Optional.of(new LocationGetDto(
                        null,
                        null
                ))
        );

        when(getLocationDaoInterface.getLocationDto(addressValidId)).thenReturn(
                Optional.of(new LocationGetDto(
                        15.9,
                        20.1
                ))
        );
    }

    @AfterEach
    void closeService() throws Exception {
        autoCloseable.close();
    }



    @Test
    void get_nonexistant_address_and_get_NOT_FOUND_status(){
        Assertions.assertEquals(HttpStatus.NOT_FOUND, addressRESTController.getAddress(UUID.randomUUID()).getStatusCode());
    }

    @Test
    void get_invalid_address_and_get_BAD_REQUEST_status(){
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, addressRESTController.getAddress(addressInvalidId).getStatusCode());
    }

    @Test
    void get_valid_address_and_get_OK_status(){
        Assertions.assertEquals(HttpStatus.OK, addressRESTController.getAddress(addressValidId).getStatusCode());
    }

    @Test
    void get_nonexistant_location_and_get_NOT_FOUND_status(){
        Assertions.assertEquals(HttpStatus.NOT_FOUND, addressRESTController.getLocationByAddressId(UUID.randomUUID()).getStatusCode());
    }

    @Test
    void get_invalid_location_and_get_BAD_REQUEST_status(){
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, addressRESTController.getLocationByAddressId(addressInvalidId).getStatusCode());
    }

    @Test
    void get_valid_location_and_get_OK_status(){
        Assertions.assertEquals(HttpStatus.OK, addressRESTController.getLocationByAddressId(addressValidId).getStatusCode());
    }


}
