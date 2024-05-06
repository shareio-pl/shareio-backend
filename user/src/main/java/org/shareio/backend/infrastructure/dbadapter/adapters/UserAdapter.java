package org.shareio.backend.infrastructure.dbadapter.adapters;

import jakarta.transaction.Transactional;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveUserCommandInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.UserDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserAdapter implements GetUserProfileDaoInterface, RemoveUserCommandInterface {
    final UserRepository userRepository;

    public UserAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserProfileGetDto> getUserDto(UUID id) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(id);
        if (userEntityOptional.isEmpty()) {
            throw new NoSuchElementException();
        }
        return userEntityOptional.map(UserDatabaseMapper::toDto);
    }

    @Override
    @Transactional
    public void removeUser(UUID userId) {
        userRepository.delete(userRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new));
    }
}
