package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDate;

public record UserAddDto
        (
                String name,
                String surname,
                String email,
                LocalDate dateOfBirth,
                String password,
                String country,
                String region,
                String city,
                String postcode,
                String street,
                String houseNumber,
                String flatNumber
        ) {
}
