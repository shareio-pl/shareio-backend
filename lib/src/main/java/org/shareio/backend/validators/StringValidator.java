package org.shareio.backend.validators;

import org.shareio.backend.Const;
import org.shareio.backend.exceptions.ValidationException;

import java.util.Objects;

public class StringValidator {
    public static void validateStringNotEmpty(String string) throws ValidationException {
        if (Objects.isNull(string) || string.isBlank() || string.isEmpty())
            throw new ValidationException("String is empty");
    }

    public static void validateEmail(String email) throws ValidationException {
        if (!Const.emailRegex.matcher(email).find())
            throw new ValidationException("Malformed email");
    }
}
