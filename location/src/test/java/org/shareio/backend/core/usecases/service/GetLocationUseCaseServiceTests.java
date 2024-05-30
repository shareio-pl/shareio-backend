package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;


class GetLocationUseCaseServiceTests {
    AutoCloseable test_autoCloseable;
    UUID test_failId;
    UUID test_correctId;

    @Mock
    GetLocationDaoInterface test_getLocationDaoInterface;

    @InjectMocks
    GetLocationUseCaseService test_getLocationUseCaseService;

    @BeforeEach
    public void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
        test_failId = UUID.randomUUID();
        test_correctId = UUID.randomUUID();
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_nonexistent_location_and_throw_NoSuchElementException() {
        when(test_getLocationDaoInterface.getLocationDto(test_failId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_getLocationUseCaseService.getLocationResponseDto(test_failId)
        );
    }

    @Test
    void get_existent_location_and_throw_MultipleValidationException() {
        when(test_getLocationDaoInterface.getLocationDto(test_failId)).thenReturn(Optional.of(new LocationGetDto(null, 1.0)));
        Assertions.assertThrows(MultipleValidationException.class,
                () -> test_getLocationUseCaseService.getLocationResponseDto(test_failId)
        );
    }

    @Test
    void get_existent_location_and_succeed() {
        when(test_getLocationDaoInterface.getLocationDto(test_failId)).thenReturn(Optional.of(new LocationGetDto(1.0, 1.0)));
        LocationResponseDto locationResponseDto = Assertions.assertDoesNotThrow(
                () -> test_getLocationUseCaseService.getLocationResponseDto(test_failId)
        );
        Assertions.assertNotNull(locationResponseDto);
    }
}
