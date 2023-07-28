package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(int catId);

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    CategoryDto patchCategory(CategoryDto categoryDto, int catId);

    void deleteCategory(int catId);

    Category findCategoryById(int catId);
}
