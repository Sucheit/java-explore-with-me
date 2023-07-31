package ru.practicum.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatisticsClient extends BaseClient {
    private static final String API_PREFIX_HIT = "/hit";
    private static final String API_PREFIX_STATS = "/stats";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatisticsClient(@Value("${statistic-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("GET /stats request: start={}, end={}, uris={}, uniques={}", start, end, uris, unique);
        String urisString = String.join(",", uris);
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_TIME_FORMATTER),
                "end", end.format(DATE_TIME_FORMATTER),
                "unique", unique,
                "uris", urisString
        );
        return get(API_PREFIX_STATS + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> createHit(EndpointHitDto endpointHitDto) {
        log.info("POST /hit request: {}", endpointHitDto);
        return post(API_PREFIX_HIT, endpointHitDto);
    }

    public void createHit2(EndpointHitDto endpointHitDto) {
        log.info("POST /hit request: {}", endpointHitDto);
        WebClient.Builder builder = WebClient.builder();
        builder.build()
                .post()
                .uri(uriBuilder -> uriBuilder.scheme("http").host("localhost").port(9090).path("/hit").build())
//                .uri(URI.create("http://localhost:9090/hit"))
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(BodyInserters.fromValue(endpointHitDto))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
