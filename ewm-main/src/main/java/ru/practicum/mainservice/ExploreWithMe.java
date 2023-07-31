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
import ru.practicum.statsclient.StatisticsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_FORMATTER;

@SpringBootApplication(scanBasePackages = {"ru.practicum.mainservice", "ru.practicum.statsclient"})
public class ExploreWithMe {
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMe.class, args);
    }
}

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class TestController {

    StatisticsClient statisticsClient;

    @GetMapping(path = "/test1")
    public String test1(HttpServletRequest request) {
        log.info("Получили request={}", request);
        createEndpointHit1(request);
        log.info("Выполнили test");
        return "TEST УСПЕШЕН";
    }

    @GetMapping(path = "/test2")
    public String test2(HttpServletRequest request) {
        log.info("Получили request={}", request);
        createEndpointHit2(request);
        log.info("Выполнили test");
        return "TEST УСПЕШЕН";
    }

    private void createEndpointHit1(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        log.info("POST /hit request: {}", endpointHitDto);
        statisticsClient.createHit(endpointHitDto);
    }

    private void createEndpointHit2(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        log.info("POST /hit request: {}", endpointHitDto);
        statisticsClient.createHit2(endpointHitDto);
    }
}