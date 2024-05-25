package org.shareio.backend.core.model;

import org.shareio.backend.core.usecases.port.dto.UserModifyDto;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.exceptions.ValidationException;
import org.shareio.backend.validators.ObjectValidator;
import org.shareio.backend.validators.StringValidator;
import org.shareio.backend.Const;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserValidator {

    public static void validateUserGetDto(UserProfileGetDto userProfileGetDto) throws MultipleValidationException {
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
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateUserSaveDto(UserSaveDto userSaveDto) throws MultipleValidationException {

        Map<String, String> errorMap = new HashMap<>();
        try {
            validateName(userSaveDto.name());
        } catch (ValidationException validationException) {
            errorMap.put("Name", validationException.getMessage());
        }
        try {
            validateName(userSaveDto.surname());
        } catch (ValidationException validationException) {
            errorMap.put("Surname", validationException.getMessage());
        }
        try {
            validateEmail(userSaveDto.email());
        } catch (ValidationException validationException) {
            errorMap.put("Email", validationException.getMessage());
        }
        try {
            validateDate(userSaveDto.dateOfBirth());
        } catch (ValidationException validationException) {
            errorMap.put("DateOfBirth", validationException.getMessage());
        }
        try{
            AddressValidator.validateAddressSaveInput(
                    userSaveDto.country(),
                    userSaveDto.region(),
                    userSaveDto.city(),
                    userSaveDto.street(),
                    userSaveDto.houseNumber(),
                    userSaveDto.flatNumber(),
                    userSaveDto.postCode()
            );
        }
        catch(MultipleValidationException e){
            errorMap.putAll(e.getErrorMap());
        }
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateUserModifyDto(UserModifyDto userModifyDto) throws MultipleValidationException {

        Map<String, String> errorMap = new HashMap<>();
        try {
            validateName(userModifyDto.name());
        } catch (ValidationException validationException) {
            errorMap.put("Name", validationException.getMessage());
        }
        try {
            validateName(userModifyDto.surname());
        } catch (ValidationException validationException) {
            errorMap.put("Surname", validationException.getMessage());
        }
        try {
            validateDate(userModifyDto.dateOfBirth());
        } catch (ValidationException validationException) {
            errorMap.put("DateOfBirth", validationException.getMessage());
        }
        try{
            AddressValidator.validateAddressSaveInput(
                    userModifyDto.country(),
                    userModifyDto.region(),
                    userModifyDto.city(),
                    userModifyDto.street(),
                    userModifyDto.houseNumber(),
                    userModifyDto.flatNumber(),
                    userModifyDto.postCode()
            );
        }
        catch(MultipleValidationException e){
            errorMap.putAll(e.getErrorMap());
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

}
