package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.core.usecases.port.in.GetUserProfileUseCaseInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@AllArgsConstructor
public class UserRESTController {
    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    UserRepository userRepository;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getUserById(@RequestParam UUID id) {
        try {
            UserProfileResponseDto userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(id);
            return new CorrectResponse(userProfileResponseDto, "Correct user", HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.BAD_REQUEST);
        }
    }
}

