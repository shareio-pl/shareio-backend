package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.shareio.backend.core.model.Address;
import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.Security;
import org.shareio.backend.core.model.vo.UserId;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


class UserAdapterTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressEntity addressEntity;
    @Mock
    private SecurityEntity securityEntity;
    @Mock
    private UserSnapshot test_userSnapshot;

    @Captor
    ArgumentCaptor<UserEntity> userEntityCaptor;

    private AutoCloseable closeable;
    private UserAdapter userAdapter;
    private UUID userId;
    String userEmail;
    private UserProfileGetDto userProfileGetDto;

    UUID test_fail_user_id;
    UUID test_correct_user_id;
    String test_email = "test@test.com";
    UserEntity test_userEntity;
    String test_pw_value="1234";
    String test_string = "test";
    Double test_double = 1.0;
    LocalDate test_LocalDate = LocalDate.MIN;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        test_fail_user_id = UUID.randomUUID();
        test_correct_user_id = UUID.randomUUID();
        closeable = openMocks(this);
        userAdapter = new UserAdapter(userRepository);

        test_userEntity = new UserEntity(null, userId, test_email, "Jan", "Kowalski",
                LocalDate.of(2020, 5, 13),
                UUID.randomUUID(),
                addressEntity,
                securityEntity);


        when(securityEntity.getLastLoginDate()).thenReturn(LocalDateTime.of(2000, 12, 31, 12, 0, 0));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(test_userEntity));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(test_userEntity));
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }


    @Test
    void remove_nonexistent_user_and_throw_NoSuchElementException() {
        when(userRepository.findByUserId(test_fail_user_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> userAdapter.removeUser(test_fail_user_id));
        verify(userRepository, never()).delete(any());

    }

    @Test
    void remove_existent_user_and_succeed() {
        when(userRepository.findByUserId(test_correct_user_id)).thenReturn(Optional.of(test_userEntity));
        Assertions.assertDoesNotThrow(() -> userAdapter.removeUser(test_correct_user_id));
        verify(userRepository, atLeastOnce()).delete(any());
    }

    @Test
    void get_nonexistent_user_by_id_and_throw_NoSuchElementException() {
        UUID userIdWrong = UUID.randomUUID();
        Assert.assertThrows(NoSuchElementException.class, () -> userAdapter.getUserDto(userIdWrong));
    }

    @Test
    void get_existent_user_by_id_and_succeed(){
        when(userRepository.findByUserId(test_correct_user_id)).thenReturn(Optional.of(test_userEntity));
        when(addressEntity.getAddressId()).thenReturn(UUID.randomUUID());
        when(securityEntity.getLastLoginDate()).thenReturn(LocalDateTime.now());
        when(securityEntity.getPwHash()).thenReturn(test_pw_value);
        Optional<UserProfileGetDto> userProfileGetDtoResult = Assertions
                .assertDoesNotThrow(() -> userAdapter.getUserDto(test_correct_user_id));
        Assertions.assertNotNull(userProfileGetDtoResult);
        Assertions.assertTrue(userProfileGetDtoResult.isPresent());
    }

    @Test
    void get_nonexistent_user_by_email_and_throw_NoSuchElementException(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> userAdapter.getUserDto(test_email));
    }

    @Test
    void get_existent_user_by_email_and_succeed(){
        when(userRepository.findByEmail(test_email)).thenReturn(Optional.of(test_userEntity));
        when(addressEntity.getAddressId()).thenReturn(UUID.randomUUID());
        when(securityEntity.getLastLoginDate()).thenReturn(LocalDateTime.now());
        when(securityEntity.getPwHash()).thenReturn(test_pw_value);
        Optional<UserProfileGetDto> userProfileGetDtoResult =  Assertions
                .assertDoesNotThrow(() -> userAdapter.getUserDto(test_email));
        Assertions.assertNotNull(userProfileGetDtoResult);
        Assertions.assertTrue(userProfileGetDtoResult.isPresent());
    }



    @Test
    void modify_user_and_throw_NoSuchElementException(){
        when(test_userSnapshot.userId()).thenReturn(new UserId(test_fail_user_id));
        when(userRepository.findByUserId(test_fail_user_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userAdapter.updateUserMetadata(test_userSnapshot));
    }

    @Test
    void modify_user_and_succeed(){
        test_userEntity.setAddress(new AddressEntity());
        when(test_userSnapshot.userId()).thenReturn(new UserId(test_correct_user_id));
        when(userRepository.findByUserId(test_correct_user_id)).thenReturn(Optional.of(test_userEntity));
        when(test_userSnapshot.name()).thenReturn(test_string);
        when(test_userSnapshot.surname()).thenReturn(test_string);
        when(test_userSnapshot.dateOfBirth()).thenReturn(test_LocalDate);
        when(test_userSnapshot.address()).thenReturn(new Address(null, test_string, test_string, test_string,test_string,test_string,test_string,test_string,new Location(test_double, test_double)));
        Assertions.assertDoesNotThrow(
                () -> userAdapter.updateUserMetadata(test_userSnapshot));
        verify(userRepository, atLeastOnce()).save(any());
        verify(userRepository).save(userEntityCaptor.capture());
        Assertions.assertAll(()->
        {
            UserEntity userEntity = userEntityCaptor.getValue();
            Assertions.assertEquals(test_string, userEntity.getName());
            Assertions.assertEquals(test_string, userEntity.getSurname());
            Assertions.assertEquals(test_LocalDate, userEntity.getDateOfBirth());
            Assertions.assertEquals(test_string, userEntity.getAddress().getCountry());
            Assertions.assertEquals(test_string, userEntity.getAddress().getRegion());
            Assertions.assertEquals(test_string, userEntity.getAddress().getCity());
            Assertions.assertEquals(test_string, userEntity.getAddress().getStreet());
            Assertions.assertEquals(test_string, userEntity.getAddress().getHouseNumber());
            Assertions.assertEquals(test_string, userEntity.getAddress().getFlatNumber());
            Assertions.assertEquals(test_string, userEntity.getAddress().getPostCode());
            Assertions.assertEquals(test_double, userEntity.getAddress().getLatitude());
            Assertions.assertEquals(test_double, userEntity.getAddress().getLongitude());
        });
    }

    @Test
    void modify_user_password_and_throw_NoSuchElementException(){
        when(test_userSnapshot.userId()).thenReturn(new UserId(test_fail_user_id));
        when(userRepository.findByUserId(test_fail_user_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                ()-> userAdapter.updateUserPassword(test_userSnapshot));

    }

    @Test
    void modify_user_password_and_succeed(){
        when(test_userSnapshot.userId()).thenReturn(new UserId(test_correct_user_id));
        when(test_userSnapshot.security()).thenReturn(new Security(test_pw_value, null, null, null));
        when(userRepository.findByUserId(test_correct_user_id)).thenReturn(Optional.of(test_userEntity));
        Assertions.assertDoesNotThrow(
                ()-> userAdapter.updateUserPassword(test_userSnapshot)
        );
        verify(userRepository, atLeastOnce()).save(any());
        verify(securityEntity, atLeastOnce()).setPwHash(test_pw_value);
    }

    @Test
    void get_all_users_and_succeed(){
        when(userRepository.findAll()).thenReturn(List.of(test_userEntity));
        Assertions.assertNotNull(userAdapter.getAllUserProfileList());
        Assertions.assertFalse(userAdapter.getAllUserProfileList().isEmpty());

    }

}
