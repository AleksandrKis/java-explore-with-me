package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryRequestDto;
import ru.practicum.categories.models.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(NewCategoryRequestDto requestDto);

    CategoryDto updateCategory(long categoryId, NewCategoryRequestDto requestDto);

    CategoryDto getCategoryById(long categoryId);

    List<CategoryDto> getCategories(int from, int size);

    void deleteCategory(long categoryId);

    Category checkExist(long id);
}
