package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.model.UserSnapshot;

import java.util.Optional;

public interface SaveUserCommandInterface {
    // TODO: do something about it
    void saveUser(Optional<UserSnapshot> userSnapshot);
}
