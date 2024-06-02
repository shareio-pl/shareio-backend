package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.in.AddUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileByEmailDaoInterface;
import org.shareio.backend.core.usecases.port.out.SaveUserCommandInterface;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AddUserUseCaseService implements AddUserUseCaseInterface {

    private GetUserProfileByEmailDaoInterface getUserProfileByEmailDaoInterface;
    private SaveUserCommandInterface saveUserCommandInterface;


    @Override
    public UUID addUser(UserSaveDto userAddDto)  {
        try {
            getUserProfileByEmailDaoInterface.getUserDto(userAddDto.email());

            throw new IllegalArgumentException("User with such email already exists");

        } catch (NoSuchElementException noSuchElementException) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String pwHash = bCryptPasswordEncoder.encode(userAddDto.password());
            User user = Optional.of(userAddDto).map(User::fromDto).orElseThrow(NoSuchElementException::new);
            user.getSecurity().setPwHash(pwHash);
            try {
                user.getAddress().setLocation(LocationCalculator.getLocationFromAddress(
                        user.getAddress().getCountry(),
                        user.getAddress().getCity(),
                        user.getAddress().getStreet(),
                        user.getAddress().getHouseNumber()
                ));
            } catch (LocationCalculationException | IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
                user.getAddress().setLocation(new Location(0.0, 0.0));
            }

            saveUserCommandInterface.saveUser(Optional.of(user).map(User::toSnapshot));
            return user.getUserId().getId();
        }
    }
}
