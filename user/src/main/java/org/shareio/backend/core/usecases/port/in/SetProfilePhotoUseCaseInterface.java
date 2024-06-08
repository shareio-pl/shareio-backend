package org.shareio.backend.core.usecases.port.in;

import java.util.UUID;

public interface SetProfilePhotoUseCaseInterface {
    // Sets users photoId value, returns previous value
    UUID setProfilePhoto(UUID userId, UUID photoId);
}
