package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.model.UserSnapshot;

import java.util.UUID;

public interface GetUserProfileUseCaseInterface {
    UserSnapshot getUserSnapshot(UUID id);
}
