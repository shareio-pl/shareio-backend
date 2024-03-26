package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.Address;
import org.shareio.backend.core.model.vo.Security;
import org.shareio.backend.core.model.vo.UserId;
import org.shareio.backend.core.model.vo.UserRating;

import java.time.LocalDateTime;

public record UserSnapshot(UserId userId, String email, String name, LocalDateTime dateOfBirth, UserRating userRating,
                           Address address,
                           Security security) {
    public UserSnapshot(User user) {
        this(user.getUserId(), user.getEmail(), user.getName(), user.getDateOfBirth(), user.getUserRating(), user.getAddress(), user.getSecurity());
    }

}
