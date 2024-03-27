package org.shareio.backend.validators;

import java.util.Objects;

public class ObjectValidator {

    public static void validateOptionalIsNotEmpty(Object object){
        if(Objects.isNull(object)) throw new RuntimeException();
    }
}
