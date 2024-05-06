package org.shareio.backend.core.usecases.port.out;

import java.util.UUID;

public interface RemoveUserCommandInterface {
    void removeUser(UUID userId);
}
