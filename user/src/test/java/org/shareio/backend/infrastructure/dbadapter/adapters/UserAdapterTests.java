package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.UserDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


public class UserAdapterTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressEntity addressEntity;
    @Mock
    private SecurityEntity securityEntity;
    private AutoCloseable closeable;
    private UserAdapter userAdapter;
    private UUID userId;
    String userEmail;
    private UserProfileGetDto userProfileGetDto;


    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        userEmail = "jan.kowalski@poczta.pl";
        closeable = openMocks(this);
        UserEntity userEntity = new UserEntity(null, userId, userEmail, "Jan", "Kowalski",
                LocalDate.of(2020, 5, 13),
                UUID.randomUUID(),
                addressEntity,
                securityEntity);
        when(securityEntity.getLastLoginDate()).thenReturn(LocalDateTime.of(2000, 12, 31, 12, 0, 0));
        userProfileGetDto = Optional.of(userEntity).map(UserDatabaseMapper::toDto).get();
        userAdapter = new UserAdapter(userRepository);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetUserDtoThrows() {
        UUID userIdWrong = UUID.randomUUID();
        Assert.assertThrows(NoSuchElementException.class, () -> userAdapter.getUserDto(userIdWrong));
    }

    @Test
    public void testGetUserDtoCorrect() {
        Assertions.assertTrue(userAdapter.getUserDto(userId).isPresent());
        UserProfileGetDto actual = userAdapter.getUserDto(userId).get();
        Assertions.assertEquals(userProfileGetDto.userId(), actual.userId());
        Assertions.assertEquals(userProfileGetDto.email(), actual.email());
        Assertions.assertEquals(userProfileGetDto.name(), actual.name());
        Assertions.assertEquals(userProfileGetDto.surname(), actual.surname());
        Assertions.assertEquals(userProfileGetDto.dateOfBirth(), actual.dateOfBirth());
        Assertions.assertEquals(userProfileGetDto.photoId(), actual.photoId());
        Assertions.assertEquals(userProfileGetDto.lastLoginDate(), actual.lastLoginDate());
    }

    @Test
    public void remove_nonexistent_user_and_throw_NoSuchElementException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> userAdapter.removeUser(UUID.randomUUID()));
    }
}
