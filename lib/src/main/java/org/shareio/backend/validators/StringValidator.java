package org.shareio.backend.validators;

import java.util.Objects;

public class StringValidator {
    public static void validateStringNotEmpty(String string){
        if (Objects.isNull(string) || string.isBlank() || string.isEmpty()) throw new RuntimeException();
    }
}
