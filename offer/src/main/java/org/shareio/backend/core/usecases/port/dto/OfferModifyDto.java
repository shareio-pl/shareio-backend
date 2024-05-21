package org.shareio.backend.core.usecases.port.dto;

import java.util.UUID;

public record OfferModifyDto
        (
                UUID offerId,
                AddressSaveDto addressSaveDto,
                String title,
                String condition,
                String category,
                String description
        ){
}
