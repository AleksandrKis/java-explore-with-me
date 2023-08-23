package ru.practicum.categories.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.MapperCategoryDto;
import ru.practicum.categories.dto.NewCategoryRequestDto;
import ru.practicum.categories.models.Category;
import ru.practicum.categories.storage.CategoryRepository;
import ru.practicum.exceptions.CategoryNotFoundException;

import java.util.List;

import static ru.practicum.categories.dto.MapperCategoryDto.mapToCategory;
import static ru.practicum.categories.dto.MapperCategoryDto.mapToCategoryDto;
import static ru.practicum.utility.Constant.getPage;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepository categoryRepo;

    @Override
    public CategoryDto addNewCategory(NewCategoryRequestDto requestDto) {
        return mapToCategoryDto(categoryRepo.save(mapToCategory(requestDto)));
    }

    @Override
    public CategoryDto updateCategory(long categoryId, NewCategoryRequestDto requestDto) {
        Category toBeUpdate = checkExist(categoryId);
        toBeUpdate.setName(requestDto.getName());
        return mapToCategoryDto(categoryRepo.save(toBeUpdate));
    }

    @Override
    public CategoryDto getCategoryById(long categoryId) {
        return mapToCategoryDto(checkExist(categoryId));
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryRepo.findAll(getPage(from, size)).map(MapperCategoryDto::mapToCategoryDto).toList();
    }

    @Override
    public void deleteCategory(long categoryId) {
        categoryRepo.delete(checkExist(categoryId));
    }

    @Override
    public Category checkExist(long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("category by id- " + id + "not found"));
    }
}
