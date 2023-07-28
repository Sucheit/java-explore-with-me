package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Constants.DATE_TIME_FORMATTER;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventController {

    EventService eventService;

    StatisticsClient statisticsClient;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("GET /events request: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}," +
                        " onlyAvailable={}, sort={}, from={}, size={}", text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        List<EventShortDto> eventShortDtoList = eventService.searchEventsByUser(
                text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        log.info("Request GET /events completed: {}", eventShortDtoList);
        createEndpointHit(request);
        return eventShortDtoList;
    }

    @GetMapping("/{id}")
    public EventFullDto findById(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        log.info("GET /event/{id} request: id={}", id);
        EventFullDto eventFullDto = eventService.findEventFullDtoById(id);
        log.info("GET /event/{id} completed: {}", eventFullDto);
        createEndpointHit(request);
        return eventFullDto;
    }

    private void createEndpointHit(HttpServletRequest request) {
        statisticsClient.createHit(EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build());
    }
}
