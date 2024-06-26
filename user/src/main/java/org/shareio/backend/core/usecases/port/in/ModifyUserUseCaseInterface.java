package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.UserModifyDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface ModifyUserUseCaseInterface {
    void modifyUser(UUID userId, UserModifyDto userModifyDto) throws MultipleValidationException;
}
