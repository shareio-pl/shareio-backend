package org.shareio.backend.infrastructure.email;

public record EmailDto(
        String messageTitle,
        String messageBody
) {
}
