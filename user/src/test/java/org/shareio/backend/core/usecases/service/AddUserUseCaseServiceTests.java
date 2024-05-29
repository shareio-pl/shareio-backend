package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileByEmailDaoInterface;
import org.shareio.backend.core.usecases.port.out.SaveUserCommandInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@Slf4j
class AddUserUseCaseServiceTests {

    AutoCloseable autoCloseable;
    final String existingUserEmail = "test@test.com";
    UserSaveDto userSaveDto;

    @Mock
    GetUserProfileByEmailDaoInterface getUserProfileByEmailDaoInterface;

    @Mock
    SaveUserCommandInterface saveUserCommandInterface;


    @InjectMocks
    AddUserUseCaseService addUserUseCaseService;

    @Captor
    ArgumentCaptor<Optional<UserSnapshot>> userSnapshotArgumentCaptor;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);


        when(getUserProfileByEmailDaoInterface.getUserDto(existingUserEmail)).thenReturn(
                Optional.of(new UserProfileGetDto(
                        UUID.randomUUID(),
                        existingUserEmail,
                        "Jan",
                        "Kowal",
                        LocalDate.now(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        null
                ))
        );
        when(getUserProfileByEmailDaoInterface.getUserDto(not(eq(existingUserEmail)))).
                thenThrow(new NoSuchElementException());

    }

    @Test
    void add_user_with_existing_email_and_throw_IllegalArgumentException() {
        userSaveDto = new UserSaveDto(
                "Jan",
                "Kowal",
                "bbb",
                existingUserEmail,
                LocalDate.now(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "95-000",
                "Lutomierska",
                "12",
                "2"
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> addUserUseCaseService.addUser(userSaveDto));
    }

    @Test
    void add_user_with_no_password_and_fail() {
        userSaveDto = new UserSaveDto(
                "Jan",
                "Kowal",
                null,
                "jankowal@gmail.com",
                LocalDate.now(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "95-000",
                "Lutomierska",
                "12",
                "2"
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> addUserUseCaseService.addUser(userSaveDto));
    }

    @Test
    void add_correct_user_and_succeed() {
        userSaveDto = new UserSaveDto(
                "Jan",
                "Kowal",
                "bbb",
                "jankowal@gmail.com",
                LocalDate.now(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "95-000",
                "Lutomierska",
                "12",
                "2"
        );
        Assertions.assertAll(()-> {
            UUID userId =  Assertions.assertDoesNotThrow(() -> addUserUseCaseService.addUser(userSaveDto));
            Assertions.assertNotNull(userId);
            verify(saveUserCommandInterface, atLeastOnce()).saveUser(any());
            verify(saveUserCommandInterface).saveUser(userSnapshotArgumentCaptor.capture());
            Assertions.assertNotNull(userSnapshotArgumentCaptor.getValue().get().security().getPwHash());
        });

    }
}
