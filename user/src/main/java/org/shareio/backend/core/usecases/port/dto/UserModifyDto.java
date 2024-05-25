package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDate;

public record UserModifyDto
        (
                String name,
                String surname,
                LocalDate dateOfBirth,
                String country,
                String region,
                String city,
                String street,
                String houseNumber,
                String flatNumber,
                String postCode
        ){

}
