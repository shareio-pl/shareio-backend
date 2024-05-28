package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.model.UserSnapshot;

public interface UpdateUserChangePasswordCommandInterface {
    void updateUserPassword(UserSnapshot userSnapshot);
}
