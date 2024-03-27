package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileQueryInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.UserMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserAdapter implements GetUserProfileQueryInterface {

    final UserRepository userRepository;

    public UserAdapter(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserProfileGetDto> getUserDto(UUID id) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(id);
        if (userEntity.isEmpty()) {
            throw new RuntimeException();
        }
        return userEntity.map(UserMapper::toDto);
    }
}
