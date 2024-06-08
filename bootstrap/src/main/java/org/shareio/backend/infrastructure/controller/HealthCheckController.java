package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.security.AuthenticationHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@AllArgsConstructor
public class HealthCheckController {

    AuthenticationHandler authenticationHandler;

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public RedirectView redirectWithUsingRedirectView() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("Ok, profile: " + authenticationHandler.getActiveProfile());
    }
}
