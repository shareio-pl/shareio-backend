package org.shareio.backend.core.model;

import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.exceptions.ValidationException;
import org.shareio.backend.validators.ObjectValidator;
import org.shareio.backend.validators.StringValidator;
import org.shareio.backend.Const;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserValidator {

    public static void validateUser(UserProfileGetDto userProfileGetDto) throws MultipleValidationException {
        Map<String, String> errorMap = new HashMap<>();
        try{
            validateName(userProfileGetDto.name());
        } catch(ValidationException validationException){
            errorMap.put("Name", validationException.getMessage());
        }
        try{
            validateEmail(userProfileGetDto.email());
        } catch(ValidationException validationException){
            errorMap.put("Email", validationException.getMessage());
        }
        try{
            validateDate(userProfileGetDto.dateOfBirth());
        } catch(ValidationException validationException){
            errorMap.put("DateOfBirth", "Malformed date of birth");
        }
        try{
            validateCountry(userProfileGetDto.country());
        } catch(ValidationException validationException){
            errorMap.put("Country", validationException.getMessage());
        }
        try{
            validateCity(userProfileGetDto.city());
        } catch(ValidationException validationException){
            errorMap.put("City", validationException.getMessage());
        }
        try{
            validateDate(userProfileGetDto.lastLoginDate());
        } catch(ValidationException validationException){
            errorMap.put("LastLoginDate", "Malformed last login date");
        }
        if(!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateName(String name) throws ValidationException {
        StringValidator.validateStringNotEmpty(name);
        if (name.length()<Const.minNameLength) throw new ValidationException("Name is too short!");
        if (name.length()>Const.maxNameLength) throw new ValidationException("Name is too long!");
    }

    public static void validateEmail(String email) throws ValidationException {
        StringValidator.validateStringNotEmpty(email);
//        StringValidator.validateEmail(email);
    }

    public static void validateDate(LocalDateTime date) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(date);
    }

    public static void validateCountry(String country) throws ValidationException {
        StringValidator.validateStringNotEmpty(country);
    }

    public static void validateCity(String city) throws ValidationException {
        StringValidator.validateStringNotEmpty(city);
    }
}
