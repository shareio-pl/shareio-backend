package org.shareio.backend.core.usecases.port.dto;

public record OfferModifyDto
        (
                AddressSaveDto addressSaveDto,
                String title,
                String condition,
                String category,
                String description
        ){
}
