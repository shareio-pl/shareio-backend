package org.shareio.backend.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class MultipleValidationException extends ShareioException {
    private final  Map<String, String> errorMap;

    public MultipleValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
