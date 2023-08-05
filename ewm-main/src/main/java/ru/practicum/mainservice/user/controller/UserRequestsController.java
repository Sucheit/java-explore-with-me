package ru.practicum.mainservice.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserRequestsController {

    UserService userService;

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
