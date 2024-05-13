package org.shareio.backend.core.usecases.port.dto;

import java.util.List;

public record CategoriesResponseDto(
        List<CategoryWithDisplayName> categories
) {
}
