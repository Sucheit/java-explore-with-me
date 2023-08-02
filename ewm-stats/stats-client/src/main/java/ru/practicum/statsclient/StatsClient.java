package ru.practicum.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StatsClient {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String API_PREFIX_HIT = "/hit";

    private static final String API_PREFIX_STATS = "/stats";
    private final WebClient webClient;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        this.webClient = WebClient.create(serverUrl);
    }

    public String createHit(EndpointHitDto endpointHitDto) {
        log.info("POST /hit request: {}", endpointHitDto);

        try {
            return webClient.post()
                    .uri(API_PREFIX_HIT)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.info("Stats server does not work.");
            return "Stats server does not work.";
        }
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end,
                                           @Nullable List<String> uris, @Nullable Boolean unique) {
        log.info("GET /stats request: start={},end={},uris={},unique={}", start, end, uris, unique);
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API_PREFIX_STATS)
                            .queryParam("start", start.format(DATE_TIME_FORMATTER))
                            .queryParam("end", end.format(DATE_TIME_FORMATTER))
                            .queryParamIfPresent("uris", uris == null ? Optional.empty() : Optional.of(String.join(",", uris)))
                            .queryParamIfPresent("unique", unique == null ? Optional.empty() : Optional.of(unique))
                            .build())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ViewStatsDto>>() {
                    })
                    .block();
        } catch (Exception e) {
            log.info("Stats server does not work.");
            return Collections.emptyList();
        }
    }
}
