package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.SetProfilePhotoUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateUserChangeProfilePhotoCommandInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SetProfilePhotoUseCaseService implements SetProfilePhotoUseCaseInterface {

    private GetUserProfileDaoInterface getUserProfileDaoInterface;
    private UpdateUserChangeProfilePhotoCommandInterface updateUserChangeProfilePhotoCommandInterface;


    @Override
    public UUID setProfilePhoto(UUID userId, UUID photoId) {
        UserProfileGetDto userProfileGetDto = getUserProfileDaoInterface.getUserDto(userId).orElseThrow(NoSuchElementException::new);
        User user = Optional.of(userProfileGetDto).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        PhotoId oldPhotoId = user.getPhotoId();
        user.setPhotoId(new PhotoId(photoId));
        updateUserChangeProfilePhotoCommandInterface.updateUserProfilePhoto(user.toSnapshot());
        return oldPhotoId.getId();
    }
}
