package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.model.vo.UserId;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetUserProfileUseCaseServiceTests {

    private UUID userId = null;
    private UUID photoId = null;
    private UUID addressId = null;
    private AutoCloseable closeable;
    UserProfileGetDto expectedCorrectUserProfileGetDto = null;
    UserProfileResponseDto expectedCorrectUserProfileResponseDto = null;
    private GetUserProfileUseCaseService getUserProfileUseCaseService;


    @Mock
    GetUserProfileDaoInterface getUserDaoInterface;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        UUID incorrectUserId = UUID.randomUUID();
        photoId = UUID.randomUUID();
        addressId = UUID.randomUUID();
        closeable = openMocks(this);
        getUserProfileUseCaseService = new GetUserProfileUseCaseService(getUserDaoInterface);
        expectedCorrectUserProfileGetDto = new UserProfileGetDto(
                userId,
                "test@gmail.com",
                "John",
                "Doe",
                LocalDate.of(2020, 5, 13),
                photoId,
                addressId,
                LocalDateTime.of(2020, 5, 13, 6, 30)
        );

        expectedCorrectUserProfileResponseDto = new UserProfileResponseDto(
                new UserId(userId),
                "test@gmail.com",
                "John",
                "Doe",
                LocalDate.of(2020, 5, 13),
                new PhotoId(photoId),
                new AddressId(addressId),
                LocalDateTime.of(2020, 5, 13, 6, 30)
        );
        when(getUserDaoInterface.getUserDto(userId)).thenReturn(Optional.of(expectedCorrectUserProfileGetDto));
        when(getUserDaoInterface.getUserDto(incorrectUserId)).thenThrow(new NoSuchElementException());
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void getUserProfileResponseDtoThrowsNoSuchElementExceptionTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(UUID.randomUUID()));
    }

    @Test
    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionStringEmpty() {
        UUID incorrectStringId = UUID.randomUUID();
        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
                incorrectStringId,
                " ",
                " ",
                " ",
                LocalDate.now(),
                photoId,
                addressId,
                LocalDateTime.now()
        );
        when(getUserDaoInterface.getUserDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectStringId));
        Assertions.assertEquals(3, multipleValidationException.getErrorMap().size());
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Email"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Name"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Surname"));
    }

    @Test
    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionNameTooShort() {
        UUID incorrectStringId = UUID.randomUUID();
        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
                incorrectStringId,
                "test@gmail.com",
                "a",
                "a",
                LocalDate.now(),
                photoId,
                addressId,
                LocalDateTime.now()
        );
        when(getUserDaoInterface.getUserDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectStringId));
        Assertions.assertEquals(2, multipleValidationException.getErrorMap().size());
        Assertions.assertEquals("String too short", multipleValidationException.getErrorMap().get("Name"));
        Assertions.assertEquals("String too short", multipleValidationException.getErrorMap().get("Surname"));
    }

    @Test
    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionNameTooLong() {
        UUID incorrectStringId = UUID.randomUUID();
        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
                incorrectStringId,
                "test@gmail.com",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                LocalDate.now(),
                photoId,
                addressId,
                LocalDateTime.now()
        );
        when(getUserDaoInterface.getUserDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectStringId));
        Assertions.assertEquals(2, multipleValidationException.getErrorMap().size());
        Assertions.assertEquals("String too long", multipleValidationException.getErrorMap().get("Name"));
        Assertions.assertEquals("String too long", multipleValidationException.getErrorMap().get("Surname"));
    }

    @Test
    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionEmailIncorrect() {
        UUID incorrectStringId = UUID.randomUUID();
        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
                incorrectStringId,
                "test",
                "John",
                "Doe",
                LocalDate.now(),
                photoId,
                addressId,
                LocalDateTime.now()
        );
        when(getUserDaoInterface.getUserDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectStringId));
        Assertions.assertEquals(1, multipleValidationException.getErrorMap().size());
        Assertions.assertEquals("Invalid email", multipleValidationException.getErrorMap().get("Email"));
    }

    @Test
    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionDateMalformedException() {
        UUID incorrectDateId = UUID.randomUUID();
        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
                userId,
                "test@gmail.com",
                "John",
                "Doe",
                null,
                photoId,
                addressId,
                null
        );
        when(getUserDaoInterface.getUserDto(incorrectDateId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectDateId));
        Assertions.assertEquals(multipleValidationException.getErrorMap().size(), 1);
        Assertions.assertEquals("Object is null", multipleValidationException.getErrorMap().get("DateOfBirth"));
    }

    @Test
    public void getUserProfileResponseDtoCorrectTest() {
        Assertions.assertDoesNotThrow(() -> {
            UserProfileResponseDto actualUserProfileResponseDto = getUserProfileUseCaseService.getUserProfileResponseDto(userId);
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.userId().getId(), actualUserProfileResponseDto.userId().getId());
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.email(), actualUserProfileResponseDto.email());
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.name(), actualUserProfileResponseDto.name());
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.surname(), actualUserProfileResponseDto.surname());
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.dateOfBirth(), actualUserProfileResponseDto.dateOfBirth());
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.photoId().getId(), actualUserProfileResponseDto.photoId().getId());
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.address().getId(), actualUserProfileResponseDto.address().getId());
            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.lastLoginDate(), actualUserProfileResponseDto.lastLoginDate());
        });

    }
}
