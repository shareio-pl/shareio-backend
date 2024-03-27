package org.shareio.backend.controller.responses;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ErrorResponse extends ShareioResponse {
    public ErrorResponse(Map<String,String> body, String message, HttpStatus status) {
        super(body,message, status);
    }

    public ErrorResponse(String message, HttpStatus status) {
        super(message, status);
    }
}
