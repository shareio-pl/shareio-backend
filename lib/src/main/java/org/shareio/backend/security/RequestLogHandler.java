package org.shareio.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class RequestLogHandler {
    public static void handleRequest(HttpServletRequest request) {
        log.error("Requested endpoint: {}",request.getRequestURI());
    }
    public static void handleCorrectResponse() {
        log.error("Request was correct");
    }
    public static void handleErrorResponse(HttpStatus httpStatus, String error) {
        log.error("Request was wrong, status: {}, error message: {}",httpStatus.value(), error);
    }
}
