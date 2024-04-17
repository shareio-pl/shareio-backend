package org.shareio.backend.infrastructure.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> whoami() {
        return ResponseEntity.ok("shareio-backend");
    }

    @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("Ok");
    }
}
