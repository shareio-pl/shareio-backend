package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDate;

public record UserModifyDto
        (
                AddressSaveDto addressSaveDto,
                String name,
                String surname,
                LocalDate dateOfBirth
        ){

}
