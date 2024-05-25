package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.UserSaveDto;

import java.util.UUID;

public interface AddUserUseCaseInterface {
    UUID addUser(UserSaveDto userAddDto);
}
