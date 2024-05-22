package org.shareio.backend.infrastructure.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HealthCheckController {
    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public RedirectView redirectWithUsingRedirectView() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("Ok");
    }
}
