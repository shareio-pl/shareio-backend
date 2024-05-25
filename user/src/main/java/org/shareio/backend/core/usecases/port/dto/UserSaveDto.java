package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDate;

public record UserSaveDto
        (
                String name,
                String surname,
                String password,
                String email,
                LocalDate dateOfBirth,
                AddressSaveDto addressSaveDto
        ) {
}
