package org.shareio.backend.infrastructure.dbadapter.adapters;

import jakarta.transaction.Transactional;
import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.UserDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserAdapter implements GetUserProfileDaoInterface,
        GetUserProfileByEmailDaoInterface, RemoveUserCommandInterface, SaveUserCommandInterface,
        UpdateUserChangeMetadataCommandInterface, UpdateUserChangePasswordCommandInterface, UpdateUserChangeProfilePhotoCommandInterface, GetAllUserProfileListDaoInterface {
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
    public Optional<UserProfileGetDto> getUserDto(String email) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            throw new NoSuchElementException();
        }
        return userEntityOptional.map(UserDatabaseMapper::toDto);
    }

    @Override
    @Transactional
    public void removeUser(UUID userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(userId);
        if (userEntityOptional.isEmpty()) {
            throw new NoSuchElementException();
        }
        userRepository.delete(userEntityOptional.get());
    }

    @Override
    public void saveUser(Optional<UserSnapshot> userSnapshot) {
        UserEntity userEntity = userSnapshot.map(UserDatabaseMapper::toEntity).orElseThrow(NoSuchElementException::new);
        userRepository.save(userEntity);
    }

    @Override
    public void updateUserProfilePhoto(UserSnapshot userSnapshot) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userSnapshot.userId().getId());
        UserEntity userEntityFromDb = userEntity.orElseThrow(NoSuchElementException::new);
        userEntityFromDb.setPhotoId(userSnapshot.photoId().getId());
        userRepository.save(userEntityFromDb);
    }

    @Override
    public void updateUserMetadata(UserSnapshot userSnapshot) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userSnapshot.userId().getId());
        UserEntity userEntityFromDb = userEntity.orElseThrow(NoSuchElementException::new);
        userEntityFromDb.setName(userSnapshot.name());
        userEntityFromDb.setSurname(userSnapshot.surname());
        userEntityFromDb.setDateOfBirth(userSnapshot.dateOfBirth());
        userEntityFromDb.getAddress().setCountry(userSnapshot.address().getCountry());
        userEntityFromDb.getAddress().setRegion(userSnapshot.address().getRegion());
        userEntityFromDb.getAddress().setCity(userSnapshot.address().getCity());
        userEntityFromDb.getAddress().setStreet(userSnapshot.address().getStreet());
        userEntityFromDb.getAddress().setHouseNumber(userSnapshot.address().getHouseNumber());
        userEntityFromDb.getAddress().setFlatNumber(userSnapshot.address().getFlatNumber());
        userEntityFromDb.getAddress().setPostCode(userSnapshot.address().getPostCode());
        userEntityFromDb.getAddress().setLatitude(userSnapshot.address().getLocation().getLatitude());
        userEntityFromDb.getAddress().setLongitude(userSnapshot.address().getLocation().getLongitude());
        userRepository.save(userEntityFromDb);
    }

    @Override
    public void updateUserPassword(UserSnapshot userSnapshot) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userSnapshot.userId().getId());
        UserEntity userEntityFromDb = userEntity.orElseThrow(NoSuchElementException::new);
        userEntityFromDb.getSecurity().setPwHash(userSnapshot.security().getPwHash());
        userRepository.save(userEntityFromDb);
    }

    @Override
    public List<UserProfileGetDto> getAllUserProfileList() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream().map(UserDatabaseMapper::toDto).toList();
    }
}
