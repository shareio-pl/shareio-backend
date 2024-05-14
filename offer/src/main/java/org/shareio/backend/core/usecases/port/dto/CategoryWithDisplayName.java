package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.Category;

public record CategoryWithDisplayName(
        String category,
        String displayName
) {
    public CategoryWithDisplayName(Category category) {
        this(category.toString(), category.polishName());
    }
}
