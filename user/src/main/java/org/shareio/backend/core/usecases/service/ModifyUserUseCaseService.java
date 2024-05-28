package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.Address;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.UserModifyDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.ModifyUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateUserChangeMetadataCommandInterface;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ModifyUserUseCaseService implements ModifyUserUseCaseInterface {

    GetUserProfileDaoInterface getUserProfileDaoInterface;
    UpdateUserChangeMetadataCommandInterface updateUserChangeMetadataCommandInterface;

    @Override
    public void modifyUser(UUID userId, UserModifyDto userModifyDto) throws LocationCalculationException, IOException, InterruptedException, MultipleValidationException {
        UserValidator.validateUserModifyDto(userModifyDto);
        UserProfileGetDto userProfileGetDto = getUserProfileDaoInterface.getUserDto(userId).orElseThrow(NoSuchElementException::new);
        User user = Optional.of(userProfileGetDto).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        Address address = new Address(null, userModifyDto.country(), userModifyDto.region(), userModifyDto.city(), userModifyDto.street(), userModifyDto.houseNumber(), userModifyDto.flatNumber(), userModifyDto.postCode(), new Location(Const.DEFAULT_ADDRESS_CENTER_LAT, Const.DEFAULT_ADDRESS_CENTER_LON));
        user.setName(userModifyDto.name());
        user.setSurname(userModifyDto.surname());
        user.setDateOfBirth(userModifyDto.dateOfBirth());
        user.setAddress(address);
        user.getAddress().setLocation(LocationCalculator.getLocationFromAddress(userModifyDto.country(), userModifyDto.city(), userModifyDto.street(), userModifyDto.houseNumber()));
        updateUserChangeMetadataCommandInterface.updateUserMetadata(user.toSnapshot());
    }
}
