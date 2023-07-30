package ru.practicum.statsserver.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.service.StatisticsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsController {

    StatisticsService statisticsService;

    @PostMapping(path = "/hit")
    public ResponseEntity<String> createEndpointHit(
            @RequestBody @Valid EndpointHitDto endpointHitDto
    ) {
        log.info("POST /hit request: {}", endpointHitDto);
        statisticsService.createEndpointHit(endpointHitDto);
        log.info("POST /hit completed");
        return new ResponseEntity<>("Информация сохранена", HttpStatus.CREATED);
    }

    @GetMapping(path = "/stats")
    public List<ViewStatsDto> getEndpointHits(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        log.info("GET /stats request: start={}, end={}, uris={}, uniques={}", start, end, uris, unique);
        List<ViewStatsDto> viewStatsDtos = statisticsService.getStatistics(start, end, uris, unique);
        log.info("GET /stats completed: {}", viewStatsDtos);
        return viewStatsDtos;
    }

}
