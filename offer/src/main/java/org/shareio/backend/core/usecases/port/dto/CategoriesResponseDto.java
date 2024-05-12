package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.Category;

import java.util.List;

public record CategoriesResponseDto(
        List<Category> categories
) {
}
