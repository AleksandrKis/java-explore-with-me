package ru.practicum.categories.dto;

import ru.practicum.categories.models.Category;

public class MapperCategoryDto {

    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName()).build();
    }

    public static Category mapToCategory(NewCategoryRequestDto requestDto) {
        return Category.builder()
                .name(requestDto.getName()).build();
    }
}
