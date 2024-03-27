package org.shareio.backend.controller.responses;

import org.springframework.http.HttpStatusCode;

public class CorrectResponse extends ShareioResponse {

    public CorrectResponse(Object body, String message, HttpStatusCode status) {
        super(body, message, status);
    }
}
