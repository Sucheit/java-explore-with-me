package ru.practicum.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.category.mapper.CategoryMapper.mapCategoryToDto;
import static ru.practicum.category.mapper.CategoryMapper.mapNewCategoryDtoToCategory;
import static ru.practicum.utils.Utility.getPageRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryRepository.findAll(getPageRequest(from, size))
                .stream()
                .map(CategoryMapper::mapCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(int catId) {
        return mapCategoryToDto(findCategoryById(catId));
    }

    @Override
    public Category findCategoryById(int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%s was not found", catId)));
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return mapCategoryToDto(categoryRepository.save(mapNewCategoryDtoToCategory(newCategoryDto)));
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto, int catId) {
        Category category = findCategoryById(catId);
        category.setName(categoryDto.getName());
        return mapCategoryToDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(int catId) {
        categoryRepository.delete(findCategoryById(catId));
    }


}
