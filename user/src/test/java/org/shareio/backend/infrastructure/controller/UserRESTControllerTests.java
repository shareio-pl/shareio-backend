package org.shareio.backend.infrastructure.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileByEmailDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.SaveUserCommandInterface;
import org.shareio.backend.core.usecases.service.*;
import org.shareio.backend.security.AuthenticationHandler;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

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
    MockHttpServletRequest mockHttpServletRequest;

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
    @InjectMocks
    private ModifyUserUseCaseService modifyUserUseCaseService;

    @MockBean
    @InjectMocks
    private ChangePasswordUserUseCaseService changePasswordUseCaseService;
    @MockBean
    @InjectMocks
    private GetAllUserIdListUseCaseService getAllUserIdListUseCaseService;

    @InjectMocks
    UserRESTController userRESTController;

    @MockBean
    AuthenticationHandler authenticationHandler;

    @BeforeEach
    public void setUp() {
        mockHttpServletRequest = new MockHttpServletRequest();
        userInvalidId = UUID.randomUUID();
        userValidId = UUID.randomUUID();
        userValidEmail = "jan.kowal@onet.pl";
        autoCloseable = MockitoAnnotations.openMocks(this);
        userRESTController = new UserRESTController(
                authenticationHandler,
                addUserUseCaseService,
                changePasswordUseCaseService,
                getUserProfileUseCaseService,
                modifyUserUseCaseService,
                getAllUserIdListUseCaseService
        );

        when(getUserProfileDaoInterface.getUserDto(userInvalidId)).thenReturn(
                Optional.of(new UserProfileGetDto(
                        userInvalidId,
                        " ",
                        " ",
                        " ",
                        LocalDate.now(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        null
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
                        LocalDateTime.now(),
                        null
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
                        LocalDateTime.now(),
                        null
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
        Assertions.assertEquals(HttpStatus.NOT_FOUND, userRESTController.getUser(mockHttpServletRequest,UUID.randomUUID()).getStatusCode());
    }

    @Test
    void get_invalid_user_and_get_BAD_REQUEST_status() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, userRESTController.getUser(mockHttpServletRequest,userInvalidId).getStatusCode());
    }

    @Test
    void get_valid_user_and_get_OK_status() {
        Assertions.assertEquals(HttpStatus.OK, userRESTController.getUser(mockHttpServletRequest,userValidId).getStatusCode());
    }

    @Test
    void add_user_with_existing_email_and_get_BAD_REQUEST_status() {
        UserSaveDto userAddDto = new UserSaveDto(
                "Jan",
                "Kowal",
                "bbb",
                userValidEmail,
                LocalDate.now(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "95-000",
                "Lutomierska",
                "12",
                "2"
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, userRESTController.addUser(mockHttpServletRequest, userAddDto).getStatusCode());
    }

    @Test
    void add_user_with_correct_data_and_get_OK_status() {
        UserSaveDto userAddDto = new UserSaveDto(
                "Jan",
                "Kowal",
                "bbb",
                "jan.kowal@gmail.com",
                LocalDate.now(),
                "Polska",
                "Łódzkie",
                "Łódź",
                "Lutomierska",
                "12",
                "",
                "95-020"
        );
        Assertions.assertEquals(HttpStatus.OK, userRESTController.addUser(mockHttpServletRequest, userAddDto).getStatusCode());
    }

}
