package ru.practicum.admin.service;

import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    CategoryDto patchCategory(CategoryDto categoryDto, int catId);

    void deleteCategory(int catId);


    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUser(int userId);

    List<EventFullDto> searchEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto patchEvent(int eventId, UpdateEventAdminRequest event);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    CompilationDto patchCompilation(int compId, UpdateCompilationRequest updateCompilationRequest);
}
