package org.shareio.backend.validators;

import org.shareio.backend.exceptions.ValidationException;

import java.util.Objects;

public class DoubleValidator {
    public static void validateDoubleNotEmpty(Double d) throws ValidationException {
        if (Objects.isNull(d))
            throw new ValidationException("Double is empty");
    }
}
