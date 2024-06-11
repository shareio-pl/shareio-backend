package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.usecases.port.dto.UserPasswordDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateUserChangePasswordCommandInterface;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


@SuppressWarnings("CanBeFinal")
class ChangePasswordUserUseCaseServiceTests {

    AutoCloseable test_autoCloseable;
    UUID test_failed_user_id;
    UUID test_correct_user_id;
    UserProfileGetDto test_userProfileGetDto;
    BCryptPasswordEncoder test_bcryptPasswordEncoder;
    final String test_password_first = "1234";
    final String test_password_second = "123";
    final String test_password_third = "1233";
    final String test_exceptionMessage_first = "IllArgErr: New password must differ from the old password!";
    final String test_exceptionMessage_second = "IllArgErr: Password does not match!";

    @Mock
    GetUserProfileDaoInterface test_getUserProfileDaoInterface;

    @Mock
    UpdateUserChangePasswordCommandInterface test_updateUserChangePasswordCommandInterface;

    @Mock
    UserPasswordDto test_userPasswordDto;

    @InjectMocks
    ChangePasswordUserUseCaseService test_changePasswordUserUseCaseService;

    @Captor
    ArgumentCaptor<UserSnapshot> test_userSnapshotCaptor;


    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
        test_failed_user_id = UUID.randomUUID();
        test_correct_user_id = UUID.randomUUID();
        test_bcryptPasswordEncoder = new BCryptPasswordEncoder();
        test_userProfileGetDto = new UserProfileGetDto(
                test_correct_user_id,
                "test@gmail.com",
                "John",
                "Doe",
                LocalDate.of(2020, 5, 13),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2020, 5, 13, 6, 30),
                test_bcryptPasswordEncoder.encode(test_password_first)
        );
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_non_existent_user_and_throw_NoSuchElementException() {
        when(test_getUserProfileDaoInterface.getUserDto(test_failed_user_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> test_changePasswordUserUseCaseService.changePassword(test_failed_user_id, test_userPasswordDto)
        );
    }

    @Test
    void get_existent_user_and_identical_new_and_old_password_and_throw_IllegalArgumentException() {
        when(test_getUserProfileDaoInterface.getUserDto(test_correct_user_id)).thenReturn(Optional.of(test_userProfileGetDto));
        when(test_userPasswordDto.newPassword()).thenReturn(test_password_first);
        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class,
                () -> test_changePasswordUserUseCaseService.changePassword(test_correct_user_id, test_userPasswordDto)
        );
        Assertions.assertEquals(test_exceptionMessage_first, illegalArgumentException.getMessage());

    }

    @Test
    void get_existent_user_and_differing_old_passwords_and_throw_IllegalArgumentException() {
        when(test_getUserProfileDaoInterface.getUserDto(test_correct_user_id)).thenReturn(Optional.of(test_userProfileGetDto));
        when(test_userPasswordDto.oldPassword()).thenReturn(test_password_second);
        when(test_userPasswordDto.newPassword()).thenReturn(test_password_third);
        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class,
                () -> test_changePasswordUserUseCaseService.changePassword(test_correct_user_id, test_userPasswordDto)
        );
        Assertions.assertEquals(test_exceptionMessage_second, illegalArgumentException.getMessage());

    }

    @Test
    void get_existent_user_and_differing_old_and_new_password_and_succeed() {
        when(test_getUserProfileDaoInterface.getUserDto(test_correct_user_id)).thenReturn(Optional.of(test_userProfileGetDto));
        when(test_userPasswordDto.oldPassword()).thenReturn(test_password_first);
        when(test_userPasswordDto.newPassword()).thenReturn(test_password_third);
        Assertions.assertDoesNotThrow(
                () -> test_changePasswordUserUseCaseService.changePassword(test_correct_user_id, test_userPasswordDto)
        );
        verify(test_updateUserChangePasswordCommandInterface, times(1)).updateUserPassword(any());
        verify(test_updateUserChangePasswordCommandInterface).updateUserPassword(test_userSnapshotCaptor.capture());
        Assertions.assertTrue(test_bcryptPasswordEncoder.matches(test_password_third, test_userSnapshotCaptor.getValue().security().getPwHash()));
    }

}
