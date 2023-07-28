package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsUser(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /users/{userId}/events request: userId={}, from={}, size={}", userId, from, size);
        List<EventShortDto> eventShortDtoList = userService.getEventsByUserId(userId, from, size);
        log.info("GET /user/{userId}/events completed: {}", eventShortDtoList);
        return eventShortDtoList;
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventUser(
            @PathVariable int userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) {
        log.info("POST /users/{userId}/events request: userId={}, {}", userId, newEventDto);
        EventFullDto eventFullDto = userService.createEventByUser(userId, newEventDto);
        log.info("POST /users/{userId}/events completed: {}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventUser(
            @PathVariable int userId,
            @PathVariable int eventId
    ) {
        log.info("GET /events/{userId}/events/{eventId} request: userId={}, eventId={}", userId, eventId);
        EventFullDto eventFullDto = userService.getEventByUser(userId, eventId);
        log.info("GET /events/{userId}/events/{eventId} completed: {}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchEventByUser(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest
    ) {
        log.info("PATCH /users/{userId}/events/{eventId} request: userId={}, eventId={}, {}",
                userId, eventId, updateEventUserRequest);
        EventFullDto eventFullDto = userService.patchEventByUser(userId, eventId, updateEventUserRequest);
        log.info("PATCH /users/{userId}/events/{eventId} completed: {}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsParticipationInEvent(
            @PathVariable int userId,
            @PathVariable int eventId
    ) {
        log.info("GET /users/{userId}/events/{eventId}/requests request: userId={}, eventId={}", userId, eventId);
        List<ParticipationRequestDto> participationRequestDtos = userService.getParticipationRequests(userId, eventId);
        log.info("GET /users/{userId}/events/{eventId}/requests completed: {}", participationRequestDtos);
        return participationRequestDtos;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventByUser(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest request
    ) {
        log.info("PATCH /users/{userId}/events{eventId}/requests request: userId={}, eventId={}, request={}",
                userId, eventId, request);
        EventRequestStatusUpdateResult result = userService.patchEventByUser(userId, eventId, request);
        log.info("PATCH /users/{userId}/events{eventId}/requests completed: {}", result);
        return result;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(
            @PathVariable int userId
    ) {
        log.info("GET /users/{userId}/requests request: userId={}", userId);
        List<ParticipationRequestDto> participationRequestDtoList = userService.getParticipationRequests(userId);
        log.info("GET /users/{userId}/requests completed: {}", participationRequestDtoList);
        return participationRequestDtoList;
    }

    @PostMapping("{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(
            @PathVariable int userId,
            @RequestParam int eventId
    ) {
        log.info("POST /users/{userId}/requests request: userId={}, eventId={}", userId, eventId);
        ParticipationRequestDto participationRequestDto = userService.createParticipationRequest(userId, eventId);
        log.info("POST /users/{userId}/requests completed: {}", participationRequestDto);
        return participationRequestDto;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable int userId,
            @PathVariable int requestId
    ) {
        log.info("PATCH /users/{userId}/requests/{requestId}/cancel request: userId={}, requestId={}",
                userId, requestId);
        ParticipationRequestDto participationRequestDto = userService.cancelParticipationRequest(userId, requestId);
        log.info("PATCH /users/{userId}/requests/{requestId}/cancel completed: {}", participationRequestDto);
        return participationRequestDto;
    }
}
