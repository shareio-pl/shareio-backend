package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.UserModifyDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateUserChangeMetadataCommandInterface;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ModifyUserUseCaseServiceTests {

    AutoCloseable test_autoCloseable;
    UUID test_failId;
    UUID test_correctId;

    @Mock
    GetUserProfileDaoInterface test_getUserProfileDaoInterface;

    @Mock
    UpdateUserChangeMetadataCommandInterface test_updateUserChangeMetadataCommandInterface;

    @Mock
    UserModifyDto test_userModifyDto;

    @Mock
    UserProfileGetDto test_userProfileGetDto;

    @InjectMocks
    ModifyUserUseCaseService test_modifyUserUseCaseService;

    @Captor
    ArgumentCaptor<UserSnapshot> test_userSnapshotCaptor;

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
    void get_invalid_dto_and_throw_MultipleValidationException() {
        try (MockedStatic<UserValidator> utilities = Mockito.mockStatic(UserValidator.class)) {
            utilities.when(() -> UserValidator.validateUserModifyDto(test_userModifyDto))
                    .thenThrow(MultipleValidationException.class);
            Assertions.assertThrows(MultipleValidationException.class,
                    () -> test_modifyUserUseCaseService.modifyUser(test_failId, test_userModifyDto));
        }
    }

    @Test
    void get_valid_dto_for_nonexistent_user_and_throw_NoSuchElementException() {
        test_userModifyDto = new UserModifyDto(
                "Jan",
                "Kowal",
                LocalDate.now(),
                "Poland",
                "Łódzkie",
                "Łódź",
                "Drewnowska",
                "12",
                "5",
                "90-000"
        );
        when(test_getUserProfileDaoInterface.getUserDto(test_failId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_modifyUserUseCaseService.modifyUser(test_failId, test_userModifyDto));
    }

    @SneakyThrows
    @Test
    void get_valid_dto_for_existent_user_and_succeed() {
        LocalDate test_localDate = LocalDate.now();
        Location test_location = LocationCalculator.getLocationFromAddress(
                "Polska",
                "Łódzkie",
                "Łódź",
                "Drewnowska"
        );
        test_userModifyDto = new UserModifyDto(
                "Jan",
                "Kowal",
                test_localDate,
                "Poland",
                "Łódzkie",
                "Łódź",
                "Drewnowska",
                "12",
                "5",
                "90-000"
        );
        test_userProfileGetDto = new UserProfileGetDto(
                test_correctId,
                "test@gmail.com",
                "John",
                "Doe",
                LocalDate.of(2020, 5, 13),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2020, 5, 13, 6, 30),
                "1234"
        );
        when(test_getUserProfileDaoInterface.getUserDto(test_correctId)).thenReturn(Optional.of(test_userProfileGetDto));
        Assertions.assertDoesNotThrow(
                () -> test_modifyUserUseCaseService.modifyUser(test_correctId, test_userModifyDto));
        verify(test_updateUserChangeMetadataCommandInterface, times(1)).updateUserMetadata(any());
        verify(test_updateUserChangeMetadataCommandInterface).updateUserMetadata(test_userSnapshotCaptor.capture());
        UserSnapshot test_userSnapshot = test_userSnapshotCaptor.getValue();
        Assertions.assertEquals("Jan", test_userSnapshot.name());
        Assertions.assertEquals("Kowal", test_userSnapshot.surname());
        Assertions.assertEquals(test_localDate, test_userSnapshot.dateOfBirth());
        Assertions.assertEquals("Poland", test_userSnapshot.address().getCountry());
        Assertions.assertEquals("Łódzkie", test_userSnapshot.address().getRegion());
        Assertions.assertEquals("Łódź", test_userSnapshot.address().getCity());
        Assertions.assertEquals("Drewnowska", test_userSnapshot.address().getStreet());
        Assertions.assertEquals("12", test_userSnapshot.address().getHouseNumber());
        Assertions.assertEquals("5", test_userSnapshot.address().getFlatNumber());
        Assertions.assertEquals("90-000", test_userSnapshot.address().getPostCode());
        Assertions.assertEquals(Math.round(test_location.getLatitude()), Math.round(test_userSnapshot.address().getLocation().getLatitude()));
        Assertions.assertEquals(Math.round(test_location.getLongitude()), Math.round(test_userSnapshot.address().getLocation().getLongitude()));
    }
}
