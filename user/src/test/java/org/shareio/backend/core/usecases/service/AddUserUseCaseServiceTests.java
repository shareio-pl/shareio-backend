package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.usecases.port.dto.AddressSaveDto;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class AddUserUseCaseServiceTests {

    AutoCloseable autoCloseable;
    final String existingUserEmail = "test@test.com";
    UserSaveDto userSaveDto;

    @Mock
    GetUserProfileByEmailDaoInterface getUserProfileByEmailDaoInterface;

    @Mock
    SaveUserCommandInterface saveUserCommandInterface;

    @InjectMocks
    AddUserUseCaseService addUserUseCaseService;

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
                        LocalDateTime.now()
                ))
        );
        when(getUserProfileByEmailDaoInterface.getUserDto(not(eq(existingUserEmail)))).
                thenThrow(new NoSuchElementException());
    }

    @Test
    public void add_user_with_existing_email_and_throw_IllegalArgumentException() {
        userSaveDto = new UserSaveDto(
                "Jan",
                "Kowal",
                "bbb",
                existingUserEmail,
                LocalDate.now(),
                new AddressSaveDto(
                        "Polska",
                        "Łódzkie",
                        "Łódź",
                        "95-000",
                        "Lutomierska",
                        "12",
                        "2"
                )

        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> addUserUseCaseService.addUser(userSaveDto));
    }

    @Test
    public void add_correct_user_and_succeed() {
        userSaveDto = new UserSaveDto(
                "Jan",
                "Kowal",
                "bbb",
                "jankowal@gmail.com",
                LocalDate.now(),
                new AddressSaveDto(
                        "Polska",
                        "Łódzkie",
                        "Łódź",
                        "95-000",
                        "Lutomierska",
                        "12",
                        "2"
                )

        );
        Assertions.assertDoesNotThrow(() -> addUserUseCaseService.addUser(userSaveDto));
    }
}
