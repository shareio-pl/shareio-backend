package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.GetAllUserIdListUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllUserProfileListDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GetAllUserIdListUseCaseService implements GetAllUserIdListUseCaseInterface {
    GetAllUserProfileListDaoInterface getAllUserProfileListDaoInterface;

    @Override
    public List<UUID> getAllUserIdList() {
        List<UserProfileGetDto> userProfileGetDtoList = getAllUserProfileListDaoInterface.getAllUserProfileList();
        userProfileGetDtoList = userProfileGetDtoList.stream().filter(user -> {
            try {
                UserValidator.validateUserGetDto(user);
                return true;
            } catch (MultipleValidationException e) {
                return false;
            }
        }).toList();

        return userProfileGetDtoList
                .stream()
                .map(User::fromDto)
                .map(User::toSnapshot)
                .map(user -> user.userId().getId())
                .toList();
    }
}
