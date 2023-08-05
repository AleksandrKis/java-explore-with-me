package ru.practicum.categories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryRequestDto;
import ru.practicum.categories.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryController {
    static final String ADMIN_PATH = "/admin/categories";
    static final String CATEGORY_ID = "/{catId}";
    static final String PUBLIC_PATH = "/categories";
    static final String ANSWER = "CategoryController:Received request on ";
    final CategoryService categoryService;

    @PostMapping(ADMIN_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryRequestDto requestDto) {
        log.info(ANSWER + "add new category name: {}", requestDto);
        return categoryService.addNewCategory(requestDto);
    }

    @PatchMapping(ADMIN_PATH + CATEGORY_ID)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable long catId, @Valid @RequestBody NewCategoryRequestDto requestDto) {
        log.info(ANSWER + "update category ID: {}", catId);
        return categoryService.updateCategory(catId, requestDto);
    }

    @DeleteMapping(ADMIN_PATH + CATEGORY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info(ANSWER + "delete category ID: {}", catId);
        categoryService.deleteCategory(catId);
    }

    @GetMapping(PUBLIC_PATH)
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                    @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(ANSWER + "get all categories");
        return categoryService.getCategories(from, size);
    }

    @GetMapping(PUBLIC_PATH + CATEGORY_ID)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable long catId) {
        log.info(ANSWER + "delete category ID: {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
