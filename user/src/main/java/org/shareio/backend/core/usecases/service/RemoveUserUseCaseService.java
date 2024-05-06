package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;

import org.shareio.backend.core.usecases.port.in.RemoveUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.RemoveUserCommandInterface;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RemoveUserUseCaseService implements RemoveUserUseCaseInterface {
    RemoveUserCommandInterface removeUserCommandInterface;

    @Override
    public RemoveResponseDto removeUser(UUID userId, RemoveResponseDto removeResponseDto) {
        // TODO: REMOVE PHOTO BY SHAREI0-IMAGE
        removeUserCommandInterface.removeUser(userId);
        removeResponseDto.setDeletedUserCount(removeResponseDto.getDeletedUserCount() + 1L);
        removeResponseDto.setDeletedSecurityCount(removeResponseDto.getDeletedSecurityCount() + 1L);
        removeResponseDto.setDeletedAddressCount(removeResponseDto.getDeletedAddressCount() + 1L);

        return removeResponseDto;
    }
}
