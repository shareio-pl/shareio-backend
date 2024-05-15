package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.model.vo.Security;
import org.shareio.backend.core.model.vo.UserId;

import java.time.LocalDate;

public record UserSnapshot(UserId userId, String email, String name, String surname, LocalDate dateOfBirth,
                           PhotoId photoId, Address address, Security security) {
    public UserSnapshot(User user) {
        this(user.getUserId(), user.getEmail(), user.getName(), user.getSurname(), user.getDateOfBirth(), user.getPhotoId(), user.getAddress(), user.getSecurity());
    }
}
