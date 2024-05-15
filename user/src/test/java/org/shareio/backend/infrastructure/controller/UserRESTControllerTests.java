package org.shareio.backend.infrastructure.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.usecases.port.dto.UserAddDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileByEmailDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.SaveUserCommandInterface;
import org.shareio.backend.core.usecases.service.AddUserUseCaseService;
import org.shareio.backend.core.usecases.service.GetUserProfileUseCaseService;
import org.shareio.backend.core.usecases.service.RemoveUserUseCaseService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UserRESTControllerTests {

    AutoCloseable autoCloseable;
    UUID userInvalidId;
    UUID userValidId;
    String userValidEmail;

    @Mock
    private GetUserProfileDaoInterface getUserProfileDaoInterface;

    @MockBean
    @InjectMocks
    private GetUserProfileUseCaseService getUserProfileUseCaseService;

    @Mock
    private GetUserProfileByEmailDaoInterface getUserProfileByEmailDaoInterface;

    @Mock
    private SaveUserCommandInterface saveUserCommandInterface;

    @MockBean
    @InjectMocks
    private AddUserUseCaseService addUserUseCaseService;

    @MockBean
    private RemoveUserUseCaseService removeUserUseCaseService;

    @InjectMocks
    UserRESTController userRESTController;

    @BeforeEach
    public void setUp() {
        userInvalidId = UUID.randomUUID();
        userValidId = UUID.randomUUID();
        userValidEmail = "jan.kowal@onet.pl";
        autoCloseable = MockitoAnnotations.openMocks(this);
        userRESTController = new UserRESTController(
                getUserProfileUseCaseService,
                removeUserUseCaseService,
                addUserUseCaseService);

        when(getUserProfileDaoInterface.getUserDto(userInvalidId)).thenReturn(
                Optional.of(new UserProfileGetDto(
                        userInvalidId,
                        " ",
                        " ",
                        " ",
                        LocalDate.now(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDateTime.now()
                ))
        );

        when(getUserProfileDaoInterface.getUserDto(userValidId)).thenReturn(
                Optional.of(new UserProfileGetDto(
                        userValidId,
                        "jk@onet.pl",
                        "Jan",
                        "Doe",
                        LocalDate.now(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDateTime.now()
                ))
        );

        when(getUserProfileByEmailDaoInterface.getUserDto(userValidEmail)).thenReturn(
                Optional.of(new UserProfileGetDto(
                        userValidId,
                        userValidEmail,
                        "Jan",
                        "Doe",
                        LocalDate.now(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDateTime.now()
                ))
        );

        when(getUserProfileByEmailDaoInterface.getUserDto(not(eq(userValidEmail)))).
                thenThrow(new NoSuchElementException());
    }

    @AfterEach
    void closeService() throws Exception {
        autoCloseable.close();
    }

    @Test
    void get_nonexistent_user_and_get_NOT_FOUND_status() {
        Assertions.assertEquals(HttpStatus.NOT_FOUND, userRESTController.getUser(UUID.randomUUID()).getStatusCode());
    }

    @Test
    void get_invalid_user_and_get_BAD_REQUEST_status() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, userRESTController.getUser(userInvalidId).getStatusCode());
    }

    @Test
    void get_valid_user_and_get_OK_status() {
        Assertions.assertEquals(HttpStatus.OK, userRESTController.getUser(userValidId).getStatusCode());
    }

    @Test
    void add_user_with_existing_email_and_get_BAD_REQUEST_status() {
        UserAddDto userAddDto = new UserAddDto(
                "Jan",
                "Kowal",
                userValidEmail,
                LocalDate.now(),
                "bbb",
                "Polska",
                "Łódzkie",
                "Łódź",
                "95-000",
                "Lutomierska",
                "12",
                "2"
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, userRESTController.addUser(userAddDto).getStatusCode());
    }

    @Test
    void add_user_with_correct_data_and_get_OK_status() {
        UserAddDto userAddDto = new UserAddDto(
                "Jan",
                "Kowal",
                "jan.kowal@gmail.com",
                LocalDate.now(),
                "bbb",
                "Polska",
                "Łódzkie",
                "Łódź",
                "95-000",
                "Lutomierska",
                "12",
                "2"
        );
        Assertions.assertEquals(HttpStatus.OK, userRESTController.addUser(userAddDto).getStatusCode());
    }

}