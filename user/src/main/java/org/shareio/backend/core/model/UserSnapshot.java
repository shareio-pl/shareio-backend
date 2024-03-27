package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.Security;
import org.shareio.backend.core.model.vo.UserId;

import java.time.LocalDateTime;

public record UserSnapshot(UserId userId, String email, String name, LocalDateTime dateOfBirth,
                           Address address, Security security) {
    public UserSnapshot(User user) {
        this(user.getUserId(), user.getEmail(), user.getName(), user.getDateOfBirth(), user.getAddress(), user.getSecurity());
    }
}
