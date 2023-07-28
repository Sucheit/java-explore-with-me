package ru.practicum.admin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminServiceImpl implements AdminService {

    CategoryService categoryService;

    UserService userService;

    EventService eventService;

    CompilationService compilationService;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto, int catId) {
        return categoryService.patchCategory(categoryDto, catId);
    }

    @Override
    public void deleteCategory(int catId) {
        categoryService.deleteCategory(catId);
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return userService.getUsers(ids, from, size);
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return userService.createUser(newUserRequest);
    }

    @Override
    public void deleteUser(int userId) {
        userService.deleteUser(userId);
    }

    @Override
    public List<EventFullDto> searchEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @Override
    public EventFullDto patchEvent(int eventId, UpdateEventAdminRequest event) {
        return eventService.patchEventByAdmin(eventId, event);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        return compilationService.createCompilation(newCompilationDto);
    }

    @Override
    public void deleteCompilation(int comId) {
        compilationService.deleteCompilation(comId);
    }

    @Override
    public CompilationDto patchCompilation(int comId, UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.patchCompilation(comId, updateCompilationRequest);
    }
}
