package ru.practicum.admin.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.admin.service.AdminService;
import ru.practicum.event.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminEventsController {

    AdminService adminService;

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
        log.info("GET /admin/events request: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}," +
                " from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
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
}
