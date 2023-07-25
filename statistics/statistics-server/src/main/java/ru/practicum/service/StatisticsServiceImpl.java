package ru.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.mapper.StatisticsMapper.mapEndpointDtoToEntity;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticsServiceImpl implements StatisticsService {

    StatisticsRepository statisticsRepository;

    @Override
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = mapEndpointDtoToEntity(endpointHitDto);
        log.info("сохраняем {}", endpointHit);
        statisticsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Пришло {}, {}, {}, {}:", start, end, uris, unique);
        if (uris == null || uris.isEmpty()) {
            return unique ?
                    statisticsRepository.findViewStatsDtoWithoutUrisAndUniqueIp(start, end) :
                    statisticsRepository.findViewStatsDtoWithoutUris(start, end);
        } else {
            return unique ?
                    statisticsRepository.findViewStatsDtoWithUrisAndUniqueIp(start, end, uris) :
                    statisticsRepository.findViewStatsDtoWithUris(start, end, uris);
        }
    }
}