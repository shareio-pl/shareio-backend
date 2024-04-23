package org.shareio.backend.core.usecases.port.dto;

import java.util.List;

public record ConditionsResponseDto(
        List<ConditionWithDisplayName> conditions
) {
}
