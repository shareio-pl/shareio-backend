package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.Condition;

import java.util.List;

public record ConditionsResponseDto(
        List<Condition> conditions
) {
}
