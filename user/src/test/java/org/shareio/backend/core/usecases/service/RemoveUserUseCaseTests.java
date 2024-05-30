package org.shareio.backend.core.usecases.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.out.RemoveUserCommandInterface;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RemoveUserUseCaseTests {

    AutoCloseable test_autoCloseable;
    UUID test_failId;
    UUID test_correctId;

    @Mock
    RemoveUserCommandInterface test_removeUserCommandInterface;

    @InjectMocks
    RemoveUserUseCaseService test_removeUserUseCaseService;

    @BeforeEach
    public void setUp() {
        test_autoCloseable = MockitoAnnotations.openMocks(this);
        test_failId = UUID.randomUUID();
        test_correctId = UUID.randomUUID();
    }

    @AfterEach
    void closeService() throws Exception {
        test_autoCloseable.close();
    }

    @Test
    void remove_user_and_succeed(){
        RemoveResponseDto removeResponseDto = new RemoveResponseDto();
        removeResponseDto = test_removeUserUseCaseService.removeUser(test_correctId, removeResponseDto);
        verify(test_removeUserCommandInterface, times(1)).removeUser(test_correctId);
        Assertions.assertEquals(1, removeResponseDto.getDeletedUserCount());
        Assertions.assertEquals(1, removeResponseDto.getDeletedSecurityCount());
        Assertions.assertEquals(1, removeResponseDto.getDeletedAddressCount());
    }
}
