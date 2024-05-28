package org.shareio.backend.core.usecases.port.in;


import org.shareio.backend.core.usecases.port.dto.UserPasswordDto;

import java.util.UUID;


public interface ChangePasswordUserUseCaseInterface {

    void changePassword(UUID userId, UserPasswordDto userPasswordDto) throws IllegalArgumentException;

}
