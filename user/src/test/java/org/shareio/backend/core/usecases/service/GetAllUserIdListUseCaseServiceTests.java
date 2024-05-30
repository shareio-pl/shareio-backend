package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetAllUserProfileListDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

class GetAllUserIdListUseCaseServiceTests {

    AutoCloseable test_autoCloseable;
    UUID test_correct_user_id_first;
    UUID test_correct_user_id_second;

    @Mock
    UserProfileGetDto test_userProfileGetDto_first;
    @Mock
    UserProfileGetDto test_userProfileGetDto_second;
    @Mock
    GetAllUserProfileListDaoInterface test_getAllUserProfileListDaoInterface;

    @InjectMocks
    GetAllUserIdListUseCaseService test_getAllUserIdListUseCaseService;

    @BeforeEach
    void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
        test_correct_user_id_first = UUID.randomUUID();
        test_correct_user_id_second = UUID.randomUUID();
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void get_no_correct_users_and_return_empty_list() {
        try (MockedStatic<UserValidator> utilities = Mockito.mockStatic(UserValidator.class)) {
            utilities.when(() -> UserValidator.validateUserGetDto(test_userProfileGetDto_first))
                    .thenThrow(MultipleValidationException.class);
            utilities.when(() -> UserValidator.validateUserGetDto(test_userProfileGetDto_second))
                    .thenThrow(MultipleValidationException.class);
            when(test_getAllUserProfileListDaoInterface.getAllUserProfileList()).thenReturn(List.of(test_userProfileGetDto_first, test_userProfileGetDto_second));
            Assertions.assertTrue(test_getAllUserIdListUseCaseService.getAllUserIdList().isEmpty());
        }
    }

    @Test
    void get_one_correct_user_and_return_one_element_list() {
        try (MockedStatic<UserValidator> utilities = Mockito.mockStatic(UserValidator.class)) {
            utilities.when(() -> UserValidator.validateUserGetDto(test_userProfileGetDto_first))
                    .thenThrow(MultipleValidationException.class);
            test_userProfileGetDto_second = new UserProfileGetDto(
                    test_correct_user_id_second,
                    "test@gmail.com",
                    "John",
                    "Doe",
                    LocalDate.of(2020, 5, 13),
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    LocalDateTime.of(2020, 5, 13, 6, 30),
                    "1234");
            when(test_getAllUserProfileListDaoInterface.getAllUserProfileList()).thenReturn(List.of(test_userProfileGetDto_first, test_userProfileGetDto_second));
            List<UUID> result = test_getAllUserIdListUseCaseService.getAllUserIdList();
            Assertions.assertEquals(1, result.size());
            Assertions.assertTrue(result.contains(test_correct_user_id_second));
        }
    }

    @Test
    void get_two_correct_users_and_return_two_elements_list() {
        test_userProfileGetDto_first = new UserProfileGetDto(
                test_correct_user_id_first,
                "test@gmail.com",
                "John",
                "Doe",
                LocalDate.of(2020, 5, 13),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2020, 5, 13, 6, 30),
                "1234");
        test_userProfileGetDto_second = new UserProfileGetDto(
                test_correct_user_id_second,
                "test@gmail.com",
                "John",
                "Doe",
                LocalDate.of(2020, 5, 13),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2020, 5, 13, 6, 30),
                "1234");
        when(test_getAllUserProfileListDaoInterface.getAllUserProfileList()).thenReturn(List.of(test_userProfileGetDto_first, test_userProfileGetDto_second));
        List<UUID> result = test_getAllUserIdListUseCaseService.getAllUserIdList();
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(test_correct_user_id_first));
        Assertions.assertTrue(result.contains(test_correct_user_id_second));
    }

}
