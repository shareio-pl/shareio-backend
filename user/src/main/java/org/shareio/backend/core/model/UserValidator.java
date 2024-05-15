package org.shareio.backend.core.model;

import org.shareio.backend.core.usecases.port.dto.UserAddDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.exceptions.ValidationException;
import org.shareio.backend.validators.ObjectValidator;
import org.shareio.backend.validators.StringValidator;
import org.shareio.backend.Const;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserValidator {

    public static void validateUser(UserProfileGetDto userProfileGetDto) throws MultipleValidationException {
        Map<String, String> errorMap = new HashMap<>();
        try {
            validateName(userProfileGetDto.name());
        } catch (ValidationException validationException) {
            errorMap.put("Name", validationException.getMessage());
        }
        try {
            validateName(userProfileGetDto.surname());
        } catch (ValidationException validationException) {
            errorMap.put("Surname", validationException.getMessage());
        }
        try {
            validateEmail(userProfileGetDto.email());
        } catch (ValidationException validationException) {
            errorMap.put("Email", validationException.getMessage());
        }
        try {
            validateDate(userProfileGetDto.dateOfBirth());
        } catch (ValidationException validationException) {
            errorMap.put("DateOfBirth", validationException.getMessage());
        }
        try {
            validateDateTime(userProfileGetDto.lastLoginDate());
        } catch (ValidationException validationException) {
            errorMap.put("LastLoginDate", validationException.getMessage());
        }
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateUser(UserAddDto userAddDto) throws MultipleValidationException {
        Map<String, String> errorMap = new HashMap<>();
        try {
            validateName(userAddDto.name());
        } catch (ValidationException validationException) {
            errorMap.put("Name", validationException.getMessage());
        }
        try {
            validateName(userAddDto.surname());
        } catch (ValidationException validationException) {
            errorMap.put("Surname", validationException.getMessage());
        }
        try {
            validateEmail(userAddDto.email());
        } catch (ValidationException validationException) {
            errorMap.put("Email", validationException.getMessage());
        }
        try {
            validateDate(userAddDto.dateOfBirth());
        } catch (ValidationException validationException) {
            errorMap.put("DateOfBirth", validationException.getMessage());
        }
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateName(String name) throws ValidationException {
        StringValidator.validateStringNotEmpty(name);
        StringValidator.validateStringLength(name, Const.minNameLength, Const.maxNameLength);
    }

    public static void validateEmail(String email) throws ValidationException {
        StringValidator.validateStringNotEmpty(email);
        StringValidator.validateEmail(email);
    }

    public static void validateDate(LocalDate date) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(date);
    }

    public static void validateDateTime(LocalDateTime date) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(date);
    }
}
