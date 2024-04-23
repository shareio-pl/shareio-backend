package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.Condition;

public record ConditionWithDisplayName(
        String condition,
        String displayName
) {
    public ConditionWithDisplayName(Condition condition) {
        this(condition.toString(), condition.polishName());
    }
}
