package org.shareio.backend.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.core.usecases.service.GetUserProfileUseCaseService;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.email.EmailDto;
import org.shareio.backend.infrastructure.email.EmailHandler;
import org.shareio.backend.security.AuthenticationHandler;
import org.shareio.backend.security.IdentityHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/email")
public class EmailController {

    AuthenticationHandler authenticationHandler;
    IdentityHandler identityHandler;
    GetUserProfileUseCaseService getUserProfileUseCaseService;
    EmailHandler emailHandler;

    @RequestMapping(value = "/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendHelpdeskEmail(HttpServletRequest httpServletRequest, @RequestBody EmailDto emailDto) {
        UUID userId = identityHandler.getUserIdFromHeader(httpServletRequest);
        if (authenticationHandler.authenticateRequestForUserIdentity(httpServletRequest, userId)) {
            try {
                UserProfileResponseDto userProfileResponseDto = getUserProfileUseCaseService.getUserProfileResponseDto(userId);
                emailHandler.sendHelpdeskMessage(userProfileResponseDto.email(), emailDto.messageTitle(), emailDto.messageBody());
                return new CorrectResponse(emailDto, "Message sent", HttpStatus.OK);
            } catch (NoSuchElementException | MultipleValidationException e) {
                return new ErrorResponse("Error while sending email", HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ErrorResponse("NO PERMISSIONS", HttpStatus.FORBIDDEN);

        }
    }


}
