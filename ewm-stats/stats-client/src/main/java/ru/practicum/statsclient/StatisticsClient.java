package ru.practicum.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StatisticsClient extends BaseClient {
    private static final String API_PREFIX_HIT = "/hit";
    private static final String API_PREFIX_STATS = "/stats";

    @Autowired
    public StatisticsClient(@Value("${statistic-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        log.info("GET /stats request: start={}, end={}, uris={}, uniques={}", start, end, uris, unique);
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uris", uris
        );
        return get(API_PREFIX_STATS + "?start={start}&end={}&uris={}&unique={}", parameters);
    }

    public ResponseEntity<Object> createHit(EndpointHitDto endpointHitDto) {
        log.info("POST /hit request: {}", endpointHitDto);
        return post(API_PREFIX_HIT, endpointHitDto);
    }
}
