package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;

import java.util.List;

public interface GetAllUserProfileListDaoInterface {
    List<UserProfileGetDto> getAllUserProfileList();
}
