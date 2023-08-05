package ru.practicum.mainservice.admin.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.admin.service.AdminService;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminCategoriesController {

    AdminService adminService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @RequestBody @Valid NewCategoryDto newCategoryDto
    ) {
        log.info("POST /admin/categories request: {}", newCategoryDto);
        CategoryDto categoryDto = adminService.createCategory(newCategoryDto);
        log.info("POST /admin/categories completed: {}", categoryDto);
        return categoryDto;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable int catId
    ) {
        log.info("DELETE /admin/categories/{catId} request:  id={}", catId);
        adminService.deleteCategory(catId);
        log.info("DELETE /admin/categories/{catId} completed");
    }

    @PatchMapping("/{catId}")
    public CategoryDto changeCategory(
            @PathVariable int catId,
            @RequestBody @Valid CategoryDto updateCategory
    ) {
        log.info("PATCH /admin/categories/{catId} request: id={}, CategoryDto={}", catId, updateCategory);
        CategoryDto categoryDto = adminService.patchCategory(updateCategory, catId);
        log.info("PATCH /admin/categories/{catId} completed: {}", updateCategory);
        return categoryDto;
    }
}
