package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.AddressDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.AddressRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


public class AddressAdapterTests {

    @Mock
    private AddressRepository addressRepository;
    private AutoCloseable closeable;
    private AddressAdapter addressAdapter;
    private UUID addressId = null;
    private AddressGetDto addressGetDto;

    @BeforeEach
    public void setUp() {
        addressId = UUID.randomUUID();
        AddressEntity addressEntity = new AddressEntity(null, addressId, "Polska", "Łódzkie", "Łódź",
                "Wólczańska", "215", "1", "91-001",
                51.7467613, 19.4530878);
        addressGetDto = Optional.of(addressEntity).map(AddressDatabaseMapper::toDto).get();
        closeable = openMocks(this);
        addressAdapter = new AddressAdapter(addressRepository);
        when(addressRepository.findByAddressId(addressId)).thenReturn(Optional.of(addressEntity));
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAddressDtoThrowsNoSuchElementExceptionTest() {
        UUID addressIdWrong = UUID.randomUUID();
        Assert.assertThrows(NoSuchElementException.class, () -> addressAdapter.getAddressDto(addressIdWrong));
    }

    @Test
    public void testGetAddressDtoCorrect() {
        Assertions.assertTrue(addressAdapter.getAddressDto(addressId).isPresent());
        AddressGetDto actual = addressAdapter.getAddressDto(addressId).get();
        Assertions.assertEquals(addressGetDto.addressId(), actual.addressId());
        Assertions.assertEquals(addressGetDto.country(), actual.country());
        Assertions.assertEquals(addressGetDto.region(), actual.region());
        Assertions.assertEquals(addressGetDto.city(), actual.city());
        Assertions.assertEquals(addressGetDto.street(), actual.street());
        Assertions.assertEquals(addressGetDto.houseNumber(), actual.houseNumber());
        Assertions.assertEquals(addressGetDto.flatNumber(), actual.flatNumber());
        Assertions.assertEquals(addressGetDto.postCode(), actual.postCode());
    }

    @Test
    public void get_nonexistent_location_and_throw_NoSuchElementException() {
        UUID addressIdWrong = UUID.randomUUID();
        Assert.assertThrows(NoSuchElementException.class, () -> addressAdapter.getLocationDto(addressIdWrong));
    }

    @Test
    public void get_existent_location_and_succeed() {
        Assertions.assertTrue(addressAdapter.getLocationDto(addressId).isPresent());
        LocationGetDto actual = addressAdapter.getLocationDto(addressId).get();
        Assertions.assertNotNull(actual.latitude());
        Assertions.assertNotNull(actual.longitude());

    }
}
