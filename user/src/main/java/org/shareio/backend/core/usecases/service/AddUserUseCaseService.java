package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.in.AddUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileByEmailDaoInterface;
import org.shareio.backend.core.usecases.port.out.SaveUserCommandInterface;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AddUserUseCaseService implements AddUserUseCaseInterface {

    private GetUserProfileByEmailDaoInterface getUserProfileByEmailDaoInterface;
    private SaveUserCommandInterface saveUserCommandInterface;


    @Override
    public UUID addUser(UserSaveDto userAddDto) {
        try {
            getUserProfileByEmailDaoInterface.getUserDto(userAddDto.email());

            throw new IllegalArgumentException("User with such email already exists");

        } catch (NoSuchElementException noSuchElementException) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String pwHash = bCryptPasswordEncoder.encode(userAddDto.password());
            User user = Optional.of(userAddDto).map(User::fromDto).orElseThrow(NoSuchElementException::new);
            user.getSecurity().setPwHash(pwHash);
            saveUserCommandInterface.saveUser(Optional.of(user).map(User::toSnapshot));
            return user.getUserId().getId();
        }
    }
}
