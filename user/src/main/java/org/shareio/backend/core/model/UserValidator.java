package org.shareio.backend.core.model;

import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.exceptions.ValidationException;
import org.shareio.backend.validators.StringValidator;
import org.shareio.backend.Const;

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

        //TODO: CONTINUE
        if(!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateName(String name) throws ValidationException {
        StringValidator.validateStringNotEmpty(name);
        if (name.length()<Const.minNameLength) throw new ValidationException("Name is too short!");
        if (name.length()>Const.maxNameLength) throw new ValidationException("Name is too long!");
    }
}
