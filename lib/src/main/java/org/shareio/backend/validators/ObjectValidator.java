package org.shareio.backend.validators;

import org.shareio.backend.exceptions.ValidationException;

import java.util.Objects;

public class ObjectValidator {

    public static void validateObjectIsNotNull(Object object) throws ValidationException {
        if(Objects.isNull(object)) throw new ValidationException("Object is null");
    }
}
