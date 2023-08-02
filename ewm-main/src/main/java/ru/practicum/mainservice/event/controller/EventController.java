package ru.practicum.mainservice.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.statsclient.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_FORMATTER;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventController {

    EventService eventService;

    StatsClient statsClient;

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
        createEndpointHit(request);
        List<EventShortDto> eventShortDtoList = eventService.searchEventsByUser(
                text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        log.info("Request GET /events completed: {}", eventShortDtoList);
        return eventShortDtoList;
    }

    @GetMapping("/{id}")
    public EventFullDto findById(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {
        log.info("GET /event/{id} request: id={}", id);
        createEndpointHit(request);
        EventFullDto eventFullDto = eventService.findEventFullDtoById(id);
        log.info("GET /event/{id} completed: {}", eventFullDto);
        return eventFullDto;
    }

    private void createEndpointHit(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        log.info("POST /hit request: {}", endpointHitDto);
        statsClient.createHit(endpointHitDto);
    }
}
