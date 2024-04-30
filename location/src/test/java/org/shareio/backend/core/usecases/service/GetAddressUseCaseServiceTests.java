package org.shareio.backend.core.usecases.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;
import org.shareio.backend.core.usecases.port.out.GetAddressDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
public class GetAddressUseCaseServiceTests {

    private AutoCloseable closeable;
    AddressGetDto expectedCorrectAddressGetDto = null;
    AddressResponseDto expectedCorrectAddressResponseDto = null;
    private GetAddressUseCaseService getAddressUseCaseService;


    @Mock
    GetAddressDaoInterface getAddressDaoInterface;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        UUID incorrectUserId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        closeable = openMocks(this);
        getAddressUseCaseService = new GetAddressUseCaseService(getAddressDaoInterface);
        expectedCorrectAddressGetDto = new AddressGetDto(
                addressId,
                "Polska",
                "Łódzkie",
                "Łódź",
                "Wólczańska",
                "215",
                "1",
                "91-001" );
        expectedCorrectAddressResponseDto = new AddressResponseDto(
                "Polska",
                "Łódzkie",
                "Łódź",
                "Wólczańska",
                "215",
                "1",
                "91-001"
        );


        when(getAddressDaoInterface.getAddressDto(addressId)).thenReturn(Optional.of(expectedCorrectAddressGetDto));
        when(getAddressDaoInterface.getAddressDto(incorrectUserId)).thenThrow(new NoSuchElementException());
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void getAddressResponseDtoThrowsNoSuchElementExceptionTest() {
        Assertions.assertThrows(NoSuchElementException.class, () -> getAddressUseCaseService.getAddressResponseDto(UUID.randomUUID()));
    }

    @Test
    public void getAddressResponseDtoThrowsMultipleValidationErrorExceptionStringEmpty() {
        UUID incorrectStringId = UUID.randomUUID();
        AddressGetDto expectedIncorrectNameUserProfileGetDto = new AddressGetDto(
                incorrectStringId,
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " "
        );
        when(getAddressDaoInterface.getAddressDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getAddressUseCaseService.getAddressResponseDto(incorrectStringId));
        Assertions.assertEquals(6, multipleValidationException.getErrorMap().size());
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Country"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Region"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("City"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("Street"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("HouseNumber"));
        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("FlatNumber"));
//        Assertions.assertEquals("String is empty", multipleValidationException.getErrorMap().get("PostCode"));
    }

    @Test
    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionNameTooShort() {
        UUID incorrectStringId = UUID.randomUUID();
        AddressGetDto expectedIncorrectNameUserProfileGetDto = new AddressGetDto(
                incorrectStringId,
                "%",
                "%",
                "%",
                "%",
                "%",
                "%",
                "%"
        );
        when(getAddressDaoInterface.getAddressDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getAddressUseCaseService.getAddressResponseDto(incorrectStringId));
//        Assertions.assertEquals(7, multipleValidationException.getErrorMap().size());
        Assertions.assertEquals("String contains non-letter character", multipleValidationException.getErrorMap().get("Country"));
        Assertions.assertEquals("String contains non-letter character", multipleValidationException.getErrorMap().get("Region"));
        Assertions.assertEquals("String contains non-letter character", multipleValidationException.getErrorMap().get("City"));
        Assertions.assertEquals("String contains non-letter character", multipleValidationException.getErrorMap().get("Street"));
        Assertions.assertEquals("String contains non-letter character", multipleValidationException.getErrorMap().get("HouseNumber"));
        Assertions.assertEquals("String contains non-letter character", multipleValidationException.getErrorMap().get("FlatNumber"));
        Assertions.assertEquals("String contains non-letter character", multipleValidationException.getErrorMap().get("PostCode"));
    }

//    @Test
//    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionNameTooLong() {
//        UUID incorrectStringId = UUID.randomUUID();
//        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
//                new UserId(incorrectStringId),
//                "test@gmail.com",
//                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
//                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
//                LocalDateTime.now(),
//                new PhotoId(photoId),
//                new AddressId(addressId),
//                LocalDateTime.now()
//        );
//        when(getUserDaoInterface.getUserDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
//        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectStringId));
//        Assertions.assertEquals(2, multipleValidationException.getErrorMap().size());
//        Assertions.assertEquals("Name is too long!", multipleValidationException.getErrorMap().get("Name"));
//        Assertions.assertEquals("Name is too long!", multipleValidationException.getErrorMap().get("Surname"));
//    }
//
//    @Test
//    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionEmailIncorrect() {
//        UUID incorrectStringId = UUID.randomUUID();
//        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
//                new UserId(incorrectStringId),
//                "test",
//                "John",
//                "Doe",
//                LocalDateTime.now(),
//                new PhotoId(photoId),
//                new AddressId(addressId),
//                LocalDateTime.now()
//        );
//        when(getUserDaoInterface.getUserDto(incorrectStringId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
//        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectStringId));
//        Assertions.assertEquals(1, multipleValidationException.getErrorMap().size());
//        Assertions.assertEquals("Invalid email", multipleValidationException.getErrorMap().get("Email"));
//    }
//
//    @Test
//    public void getUserProfileResponseDtoThrowsMultipleValidationErrorExceptionDateMalformedException() {
//        UUID incorrectDateId = UUID.randomUUID();
//        UserProfileGetDto expectedIncorrectNameUserProfileGetDto = new UserProfileGetDto(
//                new UserId(userId),
//                "test@gmail.com",
//                "John",
//                "Doe",
//                null,
//                new PhotoId(photoId),
//                new AddressId(addressId),
//                null
//        );
//        when(getUserDaoInterface.getUserDto(incorrectDateId)).thenReturn(Optional.of(expectedIncorrectNameUserProfileGetDto));
//        MultipleValidationException multipleValidationException = Assertions.assertThrows(MultipleValidationException.class, () -> getUserProfileUseCaseService.getUserProfileResponseDto(incorrectDateId));
//        Assertions.assertEquals(multipleValidationException.getErrorMap().size(), 2);
//        Assertions.assertEquals("Malformed date of birth", multipleValidationException.getErrorMap().get("DateOfBirth"));
//        Assertions.assertEquals("Malformed last login date", multipleValidationException.getErrorMap().get("LastLoginDate"));
//    }
//
//    @Test
//    public void getUserProfileResponseDtoCorrectTest() {
//        Assertions.assertDoesNotThrow(() -> {
//            UserProfileResponseDto actualUserProfileResponseDto = getUserProfileUseCaseService.getUserProfileResponseDto(userId);
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.userId().getId(), actualUserProfileResponseDto.userId().getId());
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.email(), actualUserProfileResponseDto.email());
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.name(), actualUserProfileResponseDto.name());
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.surname(), actualUserProfileResponseDto.surname());
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.dateOfBirth(), actualUserProfileResponseDto.dateOfBirth());
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.photoId().getId(), actualUserProfileResponseDto.photoId().getId());
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.address().getId(), actualUserProfileResponseDto.address().getId());
//            Assertions.assertEquals(expectedCorrectUserProfileResponseDto.lastLoginDate(), actualUserProfileResponseDto.lastLoginDate());
//        });

    }

