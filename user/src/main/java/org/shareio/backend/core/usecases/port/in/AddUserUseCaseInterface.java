package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.UserAddDto;

import java.util.UUID;

public interface AddUserUseCaseInterface {
    UUID addUser(UserAddDto userAddDto);
}
