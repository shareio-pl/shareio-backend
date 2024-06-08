package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;

import org.shareio.backend.core.usecases.port.in.RemoveUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveUserCommandInterface;
import org.shareio.backend.exceptions.ImageException;
import org.shareio.backend.external.image.ImageStore;
import org.shareio.backend.external.image.ImageStoreInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RemoveUserUseCaseService implements RemoveUserUseCaseInterface {
    GetUserProfileDaoInterface getUserProfileDao;
    RemoveUserCommandInterface removeUserCommandInterface;

    @Override
    public RemoveResponseDto removeUser(UUID userId, RemoveResponseDto removeResponseDto) {
        String imageServiceUrl = EnvGetter.getImage();
        ImageStoreInterface imageStore = new ImageStore(imageServiceUrl);
        User user = getUserProfileDao.getUserDto(userId).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        try {
            imageStore.DeleteImage(user.getPhotoId().getId());
        } catch (NoSuchElementException | ImageException e) {
            System.out.println("Could not delete image " + user.getPhotoId().getId());
            System.out.println(e.getMessage());
        }
        removeUserCommandInterface.removeUser(userId);
        removeResponseDto.setDeletedUserCount(removeResponseDto.getDeletedUserCount() + 1L);
        removeResponseDto.setDeletedSecurityCount(removeResponseDto.getDeletedSecurityCount() + 1L);
        removeResponseDto.setDeletedAddressCount(removeResponseDto.getDeletedAddressCount() + 1L);

        return removeResponseDto;
    }
}
