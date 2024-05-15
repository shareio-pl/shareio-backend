package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.usecases.port.dto.UserAddDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.core.usecases.port.in.AddUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.GetUserProfileUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.RemoveUserUseCaseInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/user")
public class UserRESTController {
    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    RemoveUserUseCaseInterface removeUserUseCaseInterface;
    AddUserUseCaseInterface addUserUseCaseInterface;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(@PathVariable(value = "id") UUID id) {
        try {
            UserProfileResponseDto userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(id);
            return new CorrectResponse(userProfileResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addUser(@RequestBody UserAddDto userAddDto) {
        try {
            UserValidator.validateUser(userAddDto);
            UUID createdUserUUID = addUserUseCaseInterface.addUser(userAddDto);
            return new CorrectResponse(createdUserUUID, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ErrorResponse(Const.illegalArgumentErrorCode, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyUser(@PathVariable(value = "id") UUID id) {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

}

