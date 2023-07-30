package ru.practicum.mainservice.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryController {

    CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /categories request: from={}, size={}", from, size);
        List<CategoryDto> categoryDtoList = categoryService.getCategories(from, size);
        log.info("GET /categories completed: {}", categoryDtoList);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(
            @PathVariable Integer catId
    ) {
        log.info("GET /categories/{catId} request: Id={}", catId);
        CategoryDto categoryDto = categoryService.getCategoryById(catId);
        log.info("GET /categories/{catId} completed: {}", categoryDto);
        return categoryDto;
    }
}
