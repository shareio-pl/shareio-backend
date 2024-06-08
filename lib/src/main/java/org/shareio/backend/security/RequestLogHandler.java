package org.shareio.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class RequestLogHandler {
    public static void handleRequest(HttpServletRequest request) {
        log.error("Requested endpoint: {}", request.getRequestURI());
    }

    public static void handleCorrectResponse(HttpServletRequest request) {
        log.error("Request for {} was correct", request.getRequestURI());
    }

    public static void handleErrorResponse(HttpServletRequest request, HttpStatus httpStatus, String error) {
        log.error("Request for {} went wrong, status: {}, error message: {}", request.getRequestURI(), httpStatus.value(), error);
    }

    public static void handlePeriodicTask() {
        log.error("Periodic task started");
    }

    private RequestLogHandler() {
        throw new IllegalStateException("Utility class");
    }
}
