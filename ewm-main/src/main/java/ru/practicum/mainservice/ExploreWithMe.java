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

    @GetMapping(path = "/test")
    public String test(HttpServletRequest request) {
        log.info("Получили request={}", request);
        createEndpointHit(request);
        log.info("Выполнили test");
        return "TEST УСПЕШЕН";
    }

    private void createEndpointHit(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
        log.info("POST /hit request: {}", endpointHitDto);
        statisticsClient.createHit(endpointHitDto);
    }
}