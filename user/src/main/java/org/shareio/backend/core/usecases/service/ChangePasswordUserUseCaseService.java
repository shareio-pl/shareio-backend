package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.usecases.port.dto.UserPasswordDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.ChangePasswordUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateUserChangePasswordCommandInterface;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChangePasswordUserUseCaseService implements ChangePasswordUserUseCaseInterface {

    GetUserProfileDaoInterface getUserProfileDaoInterface;
    UpdateUserChangePasswordCommandInterface updateUserChangePasswordCommandInterface;

    @Override
    public void changePassword(UUID userId, UserPasswordDto userPasswordDto) throws IllegalArgumentException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<UserProfileGetDto> userProfileGetDto = getUserProfileDaoInterface.getUserDto(userId);
        User user = userProfileGetDto.map(User::fromDto).orElseThrow(NoSuchElementException::new);
        if(bCryptPasswordEncoder.matches(userPasswordDto.newPassword(), user.getSecurity().getPwHash())){
            throw new IllegalArgumentException(Const.ILL_ARG_ERR +": New password must differ from the old password!");
        }
        if(bCryptPasswordEncoder.matches(userPasswordDto.oldPassword(), user.getSecurity().getPwHash())){
            user.getSecurity().setPwHash(bCryptPasswordEncoder.encode(userPasswordDto.newPassword()));
            updateUserChangePasswordCommandInterface.updateUserPassword(user.toSnapshot());
        }
        else {
            throw new IllegalArgumentException(Const.ILL_ARG_ERR +": Password does not match!");
        }

    }
}
