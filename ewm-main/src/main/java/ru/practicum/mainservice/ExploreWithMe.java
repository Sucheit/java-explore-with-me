package ru.practicum.mainservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsclient.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mainservice.utils.Constants.*;

@SpringBootApplication(scanBasePackages = {"ru.practicum.mainservice", "ru.practicum.statsclient"})
public class ExploreWithMe {
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMe.class, args);
    }
}

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class TestController {

    StatsClient statsClient;

    @GetMapping(path = "/test/hit")
    public String testHit(HttpServletRequest request) {
        log.info("Пришело GET /test/hit request: {}, {}, {}",
                request.getRemoteAddr(), request.getRequestURI(), request.getQueryString());
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        String response = statsClient.createHit(endpointHitDto);
        log.info("GET /test/hit completed:{}", response);
        return response;
    }

    @GetMapping(path = "/test/stats")
    public List<ViewStatsDto> testGetStats() {
        log.info("GET /test/stats request!");
        List<ViewStatsDto> viewStatsDtoList =
                statsClient.getViewStats(START_DATE, END_DATE, null, null);
        log.info("GET /test/stats completed: {}", viewStatsDtoList);
        return viewStatsDtoList;
    }

}