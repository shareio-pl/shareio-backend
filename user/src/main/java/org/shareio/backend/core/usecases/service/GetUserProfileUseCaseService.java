package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.core.usecases.port.in.GetUserProfileUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.UserInfrastructureMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetUserProfileUseCaseService implements GetUserProfileUseCaseInterface {
    GetUserProfileDaoInterface getUserDaoInterface;


    @Override
    public UserProfileResponseDto getUserProfileResponseDto(UUID id) throws MultipleValidationException, NoSuchElementException {
        Optional<UserProfileGetDto> getUserDto = getUserDaoInterface.getUserDto(id);
        UserValidator.validateUserGetDto(getUserDto.orElseThrow());
        return Optional.of(getUserDto.map(User::fromDto).get().toSnapshot()).map(UserInfrastructureMapper::toDto).get();
    }
}
