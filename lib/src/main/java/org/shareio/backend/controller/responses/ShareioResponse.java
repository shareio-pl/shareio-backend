package org.shareio.backend.controller.responses;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ShareioResponse extends ResponseEntity<Object> {
    final String message;

    public ShareioResponse(Object body, String message, HttpStatusCode status) {
        super(body, status);
        this.message = message;
    }

    public ShareioResponse(String message, HttpStatusCode status) {
        super(status);
        this.message = message;
    }
}
