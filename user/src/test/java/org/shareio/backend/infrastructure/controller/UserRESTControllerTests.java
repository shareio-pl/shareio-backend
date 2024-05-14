package org.shareio.backend.infrastructure.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.service.GetUserProfileUseCaseService;
import org.shareio.backend.core.usecases.service.RemoveUserUseCaseService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class UserRESTControllerTests {

    AutoCloseable autoCloseable;
    UUID userInvalidId;
    UUID userValidId;

    @Mock
    private GetUserProfileDaoInterface getUserProfileDaoInterface;

    @MockBean
    @InjectMocks
    private GetUserProfileUseCaseService getUserProfileUseCaseService;

    @MockBean
    @InjectMocks
    private RemoveUserUseCaseService removeUserUseCaseService;

    @InjectMocks
    UserRESTController userRESTController;

    @BeforeEach
    public void setUp() {
        userInvalidId = UUID.randomUUID();
        userValidId = UUID.randomUUID();
        autoCloseable = MockitoAnnotations.openMocks(this);
        userRESTController = new UserRESTController(
                getUserProfileUseCaseService,
                removeUserUseCaseService);

        when(getUserProfileDaoInterface.getUserDto(userInvalidId)).thenReturn(
                Optional.of(new UserProfileGetDto(
                        userInvalidId,
                        " ",
                        " ",
                        " ",
                        LocalDateTime.now(),
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
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        LocalDateTime.now()
                ))
        );
    }

    @AfterEach
    void closeService() throws Exception {
        autoCloseable.close();
    }

    @Test
    void get_nonexistent_user_and_get_NOT_FOUND_status(){
        Assertions.assertEquals(HttpStatus.NOT_FOUND, userRESTController.getUser(UUID.randomUUID()).getStatusCode());
    }

    @Test
    void get_invalid_user_and_get_BAD_REQUEST_status(){
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, userRESTController.getUser(userInvalidId).getStatusCode());
    }

    @Test
    void get_valid_user_and_get_OK_status(){
        Assertions.assertEquals(HttpStatus.OK, userRESTController.getUser(userValidId).getStatusCode());
    }



}
