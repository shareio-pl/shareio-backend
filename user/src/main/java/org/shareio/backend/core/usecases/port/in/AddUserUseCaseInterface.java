package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.exceptions.LocationCalculationException;

import java.util.UUID;

public interface AddUserUseCaseInterface {
    UUID addUser(UserSaveDto userAddDto) throws LocationCalculationException;
}
