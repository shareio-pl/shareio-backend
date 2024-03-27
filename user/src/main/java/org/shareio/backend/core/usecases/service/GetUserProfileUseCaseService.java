package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.GetUserProfileUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileQueryInterface;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetUserProfileUseCaseService implements GetUserProfileUseCaseInterface {

    GetUserProfileQueryInterface getUserQueryInterface;


    @Override
    public UserSnapshot getUserSnapshot(UUID id) {
        Optional<UserProfileGetDto> getUserDto = getUserQueryInterface.getUserDto(id);
        if (getUserDto.isEmpty()) {
            throw new RuntimeException();
        }
        return getUserDto.map(User::fromDto).get().toSnapshot();
    }
}
