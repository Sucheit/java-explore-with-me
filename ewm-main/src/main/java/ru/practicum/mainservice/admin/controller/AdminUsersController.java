package ru.practicum.mainservice.admin.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.admin.service.AdminService;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminUsersController {

    AdminService adminService;

    @GetMapping()
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Integer> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("GET /admin/users request: ids={}, from={}, size={}", ids, from, size);
        List<UserDto> userDtoList = adminService.getUsers(ids, from, size);
        log.info("GET /admin/users completed: {}", userDtoList);
        return userDtoList;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @RequestBody @Valid NewUserRequest newUserRequest
    ) {
        log.info("POST /admin/users request: {}", newUserRequest);
        UserDto userDto = adminService.createUser(newUserRequest);
        log.info("POST /admin/users completed: {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable int userId
    ) {
        log.info("DELETE /admin/users/ request: id={}", userId);
        adminService.deleteUser(userId);
        log.info("DELETE /admin/users/ completed: id={}", userId);
    }
}
