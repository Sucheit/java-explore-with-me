package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.admin.service.AdminService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    AdminService adminService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @RequestBody @Valid NewCategoryDto newCategoryDto
    ) {
        log.info("POST /admin/categories request: {}", newCategoryDto);
        CategoryDto categoryDto = adminService.createCategory(newCategoryDto);
        log.info("POST /admin/categories completed: {}", categoryDto);
        return categoryDto;
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable int catId
    ) {
        log.info("DELETE /admin/categories/{catId} request:  id={}", catId);
        adminService.deleteCategory(catId);
        log.info("DELETE /admin/categories/{catId} completed");
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto changeCategory(
            @PathVariable int catId,
            @RequestBody @Valid CategoryDto updateCategory
    ) {
        log.info("PATCH /admin/categories/{catId} request: id={}, CategoryDto={}", catId, updateCategory);
        CategoryDto categoryDto = adminService.patchCategory(updateCategory, catId);
        log.info("PATCH /admin/categories/{catId} completed: {}", updateCategory);
        return categoryDto;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Integer> ids,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /admin/users request: ids={}, from={}, size={}", ids, from, size);
        List<UserDto> userDtoList = adminService.getUsers(ids, from, size);
        log.info("GET /admin/users completed: {}", userDtoList);
        return userDtoList;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @RequestBody @Valid NewUserRequest newUserRequest
    ) {
        log.info("POST /admin/users request: {}", newUserRequest);
        UserDto userDto = adminService.createUser(newUserRequest);
        log.info("POST /admin/users completed: {}", userDto);
        return userDto;
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable int userId
    ) {
        log.info("DELETE /admin/users/ request: id={}", userId);
        adminService.deleteUser(userId);
        log.info("DELETE /admin/users/ completed: id={}", userId);
    }

    @GetMapping("/events")
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /admin/events request: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}," +
                " size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtoList = adminService.searchEvents(
                users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("GET /admin/events completed: {}", eventFullDtoList);
        return eventFullDtoList;
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEvent(
            @PathVariable int eventId,
            @RequestBody UpdateEventAdminRequest event
    ) {
        log.info("PATCH /admin/events/ request: eventId={}, {}", eventId, event);
        EventFullDto eventFullDto = adminService.patchEvent(eventId, event);
        log.info("PATCH /admin/events/ completed: {}", eventFullDto);
        return eventFullDto;
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(
            @RequestBody @Valid NewCompilationDto newCompilationDto
    ) {
        log.info("POST /admin/compilations request: {}", newCompilationDto);
        CompilationDto compilationDto = adminService.createCompilation(newCompilationDto);
        log.info("POST /admin/compilations completed: {}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(
            @PathVariable int comId
    ) {
        log.info("DELETE /admin/compilations/{compId} request: comId={}", comId);
        adminService.deleteCompilation(comId);
        log.info("DELETE /admin/compilations/{compId} completed: comId={}", comId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto patchCompilation(
            @PathVariable int comId,
            @RequestBody UpdateCompilationRequest updateCompilationRequest
    ) {
        log.info("PATCH /admin/compilations/{compId} request: compId={}, {}", comId, updateCompilationRequest);
        CompilationDto compilationDto = adminService.patchCompilation(comId, updateCompilationRequest);
        log.info("PATCH /admin/compilations/{compId} completed: {}", compilationDto);
        return compilationDto;
    }
}
