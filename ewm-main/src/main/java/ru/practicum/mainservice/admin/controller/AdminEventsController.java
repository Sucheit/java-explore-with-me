package ru.practicum.mainservice.admin.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.admin.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.admin.service.AdminService;
import ru.practicum.mainservice.event.dto.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_PATTERN;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminEventsController {

    AdminService adminService;

    @GetMapping()
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        log.info("GET /admin/events request: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}," +
                " from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtoList = adminService.searchEvents(
                users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("GET /admin/events completed: {}", eventFullDtoList);
        return eventFullDtoList;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(
            @PathVariable int eventId,
            @RequestBody @Valid UpdateEventAdminRequest event
    ) {
        log.info("PATCH /admin/events/ request: eventId={}, {}", eventId, event);
        EventFullDto eventFullDto = adminService.patchEvent(eventId, event);
        log.info("PATCH /admin/events/ completed: {}", eventFullDto);
        return eventFullDto;
    }
}
