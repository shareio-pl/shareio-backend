package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDate;

public record UserSaveDto
        (
                String name,
                String surname,
                String password,
                String email,
                LocalDate dateOfBirth,

                String country,
                String region,
                String city,
                String street,
                String houseNumber,
                String flatNumber,
                String postCode
        ) {
}
