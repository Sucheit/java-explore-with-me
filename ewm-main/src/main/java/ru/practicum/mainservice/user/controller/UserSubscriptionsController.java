package ru.practicum.mainservice.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/subscriptions")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserSubscriptionsController {

    UserService userService;

    @PostMapping("/{subscribedToId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UserDto> createSubscription(
            @PathVariable int userId,
            @PathVariable int subscribedToId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("POST /users/{userId}/subscriptions/{subscribedToId} request:" +
                " userId={}, subscribedToId={}, from={}, size={}", userId, subscribedToId, from, size);
        List<UserDto> userDtoList = userService.createSubscription(userId, subscribedToId, from, size);
        log.info("POST /user/{userId}/subscriptions/{subscribedToId} completed: {}", userDtoList);
        return userDtoList;
    }

    @DeleteMapping("/{subscribedToId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(
            @PathVariable int userId,
            @PathVariable int subscribedToId
    ) {
        log.info("DELETE /users/{userId}/subscriptions/{subscribedToId} request: userId={}, subscribedToId={}", userId, subscribedToId);
        userService.deleteSubscription(userId, subscribedToId);
        log.info("DELETE /user/{userId}/subscriptions/{subscribedToId} completed:");
    }

    @GetMapping()
    public List<UserDto> getSubscriptions(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("GET /users/{userId}/subscriptions request: userId={}, from={}, size={}", userId, from, size);
        List<UserDto> userDtoList = userService.getSubscriptions(userId, from, size);
        log.info("GET /user/{userId}/subscriptions completed: {}", userDtoList);
        return userDtoList;
    }

    @GetMapping("/events")
    public List<EventShortDto> getSubscriptionEvents(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("GET /users/{userId}/subscriptions/events request: userId={}, from={}, size={}", userId, from, size);
        List<EventShortDto> eventShortDtoList = userService.getSubscriptionsEvents(userId, from, size);
        log.info("GET /user/{userId}/subscriptions/events completed: {}", eventShortDtoList);
        return eventShortDtoList;
    }
}
